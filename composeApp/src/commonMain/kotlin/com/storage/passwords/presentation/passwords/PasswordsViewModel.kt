package com.storage.passwords.presentation.passwords

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacex.utils.UiText
import com.storage.passwords.models.mapToDomain
import com.storage.passwords.models.mapToEntity
import com.storage.passwords.repository.LocalRepository
import com.storage.passwords.repository.NetworkRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import passwords.composeapp.generated.resources.Res
import passwords.composeapp.generated.resources.error_database

class PasswordsViewModel(
    val localRepository: LocalRepository,
    val networkRepository: NetworkRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PasswordsState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<PasswordsEffect>()
    val effect = _effect.asSharedFlow()


    init {
        loadPasswords()
    }

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
                    UiText.StaticString(
                        error ?: "Unknown error"
                    )
                )
            )
        }
    }

}

