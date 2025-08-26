package com.storage.passwords.presentation.passwords

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacex.utils.UiText
import com.storage.passwords.models.PasswordItem
import com.storage.passwords.models.PasswordsEntity
import com.storage.passwords.models.mapToDomain
import com.storage.passwords.models.mapToEntity
import com.storage.passwords.repository.LocalRepository
import com.storage.passwords.repository.NetworkRepository
import com.storage.passwords.usecase.LoadFromInternetUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import passwords.composeapp.generated.resources.Res
import passwords.composeapp.generated.resources.error_database
import passwords.composeapp.generated.resources.unknown_error

class PasswordsViewModel(
    val localRepository: LocalRepository,
    val networkRepository: NetworkRepository,
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
            _state.value
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
            if (localRepository.getCount() == 0) {
                try {
                    loadFromInternetAndPutToDatabase()
                } catch (ex: Exception) {
                    errorState(ex.message)
                }

            } else {
                loadDataFromDataBaseOrError()
            }
        }
    }

    private suspend fun loadFromInternetAndPutToDatabase() {

        _effect.emit(PasswordsEffect.Loading)

        val passwords = networkRepository.getData(0)
        if (passwords.isSuccess) {
            passwords.map { passwordsResults ->
                val passwordItems = passwordsResults.map { passwordResultItem ->
                    passwordResultItem.mapToDomain()
                }
                _state.update {
                    it.copy(
                        passwordItems = passwordItems
                    )
                }
                localRepository.insertPasswords(
                    passwordItems.map { passwordItem ->
                        passwordItem.mapToEntity()
                    }
                )
            }
            _effect.emit(PasswordsEffect.LoadSuccess)
        } else {
            errorState(passwords.exceptionOrNull()?.message)
        }
    }

    private suspend fun loadDataFromDataBaseOrError() {

        _effect.emit(PasswordsEffect.Loading)

        loadFromInternetUseCase.loadFromInternet(
            onLoadSuccess = { passwordItems: List<PasswordItem> ->
                _state.update {
                    it.copy(passwordItems = passwordItems)
                }
                viewModelScope.launch {
                    _effect.emit(PasswordsEffect.LoadSuccess)
                }
            },
            onLoadError = {
                viewModelScope.launch {
                    _effect.emit(
                        PasswordsEffect.LoadError(
                            UiText.StringResource(Res.string.error_database)
                        )
                    )
                }
            }
        )
        localRepository
            .loadData()
            .catch {
                _effect.emit(
                    PasswordsEffect.LoadError(
                        UiText.StringResource(Res.string.error_database)
                    )
                )
            }
            .collect { passwordItemsEntryList ->
                _state.update {
                    it.copy(passwordItems = passwordItemsEntryList.map { passwordsEntities ->
                        passwordsEntities.mapToDomain()
                    }
                    )
                }
                _effect.emit(PasswordsEffect.LoadSuccess)
            }
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

