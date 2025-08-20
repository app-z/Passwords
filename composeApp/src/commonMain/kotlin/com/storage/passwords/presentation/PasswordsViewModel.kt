package com.storage.passwords.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.storage.passwords.models.mapToDomain
import com.storage.passwords.models.mapToEntity
import com.storage.passwords.repository.LocalRepository
import com.storage.passwords.repository.NetworkRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            is PasswordsEvent.NavigateToDetail -> { id: String ->
                navigateToDetail(id)
            }
        }
    }


    fun loadPasswords() {

        viewModelScope.launch {
            if (localRepository.getCount() == 0) {
                try {
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
                                passwordItems.map {
                                    passwordItem -> passwordItem.mapToEntity()
                                }
                            )
                            _effect.emit(PasswordsEffect.LoadSuccess)
                        }
                    } else {
                        errorState(passwords.exceptionOrNull()?.message)
                    }
                } catch (ex: Exception) {
                    errorState(ex.message)
                }

            }
        }
    }

    private fun navigateToDetail(id: String) {

    }

    fun errorState(error: String?) {
        viewModelScope.launch {
            _effect.emit(PasswordsEffect.LoadError(error ?: "Unknown error"))
        }
    }

}

