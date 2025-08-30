package com.storage.passwords.presentation.passwords

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacex.utils.UiText
import com.storage.passwords.models.mapToDomain
import com.storage.passwords.repository.LocalRepository
import com.storage.passwords.repository.NetworkRepository
import com.storage.passwords.usecase.LoadFromDataBaseUseCase
import com.storage.passwords.usecase.LoadFromInternetUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import passwords.composeapp.generated.resources.Res
import passwords.composeapp.generated.resources.error_database
import passwords.composeapp.generated.resources.unknown_error

class PasswordsViewModel(
    val loadFromDataBaseUseCase: LoadFromDataBaseUseCase,
    val loadFromInternetUseCase: LoadFromInternetUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PasswordsState())

    val state = _state
        .onStart {
            handleEvent(PasswordsEvent.LoadPasswords)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _state.value
        )

    private val _effect = MutableSharedFlow<PasswordsEffect>()
    val effect = _effect.asSharedFlow()

    fun handleEvent(event: PasswordsEvent) {
        when (event) {
            is PasswordsEvent.LoadPasswords -> {
                loadPasswords()
            }

            is PasswordsEvent.NavigateToDetail -> {
                navigateToDetail(event.itemId)
            }

            is PasswordsEvent.LoadPasswordsFromNetwork -> {
                viewModelScope.launch {
                    loadFromInternetAndPutToDatabase()
                }
            }
        }
    }


    fun loadPasswords() {
        viewModelScope.launch {
            if (loadFromInternetUseCase.getCountInDataBase() == 0) {
                loadFromInternetAndPutToDatabase()
            } else {
                loadDataFromDataBaseOrError()
            }
        }
    }

    private suspend fun loadFromInternetAndPutToDatabase() {

        if (_state.value.isLoading) return

        _state.value.isLoading = true

        loadFromInternetUseCase.loadFromInternet(
            onLoadSuccess = { passwordItems ->
                _state.update {
                    it.copy(
                        passwordItems = passwordItems
                    )
                }
                viewModelScope.launch {
                    _effect.emit(PasswordsEffect.LoadSuccess)
                }
                _state.value.isLoading = false
            },
            onLoadError = {
                _state.value.isLoading = false
                errorState(it)
            }
        )
    }

    private suspend fun loadDataFromDataBaseOrError() {

        if (_state.value.isLoading) return

        _state.value.isLoading = true

        loadFromDataBaseUseCase.loadFromDataBase(
            onLoadSuccess = { passwordItems ->
                _state.update {
                    it.copy(
                        passwordItems = passwordItems
                    )
                }
                viewModelScope.launch {
                    _state.value.isLoading = false
                    _effect.emit(PasswordsEffect.LoadFromDBSuccess)
                }

            },
            onLoadError = { error ->
                viewModelScope.launch {
                    _state.value.isLoading = false
                    _effect.emit(
                        PasswordsEffect.LoadError(
                            if (error.isNullOrEmpty()) {
                                UiText.StringResource(Res.string.unknown_error)
                            } else {
                                UiText.StaticString(error)
                            }
                        )
                    )
                }
            }

        )
    }


    private fun navigateToDetail(id: String) {
        viewModelScope.launch {
            _effect.emit(PasswordsEffect.NavigateToDetail(id))
        }
    }

    fun errorState(error: String?) {
        viewModelScope.launch {
            _effect.emit(
                PasswordsEffect.LoadError(
                    if (error != null) {
                        UiText.StaticString(
                            error
                        )
                    } else {
                        UiText.StringResource(Res.string.unknown_error)
                    }
                )
            )
        }
    }

}

