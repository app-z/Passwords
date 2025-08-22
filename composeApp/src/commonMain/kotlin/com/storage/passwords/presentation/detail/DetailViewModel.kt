package com.storage.passwords.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacex.utils.UiText
import com.storage.passwords.models.PasswordItem
import com.storage.passwords.models.mapToDomain
import com.storage.passwords.models.mapToEntity
import com.storage.passwords.repository.DispatchersRepository
import com.storage.passwords.repository.LocalRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import passwords.composeapp.generated.resources.Res
import passwords.composeapp.generated.resources.unknown_error

class DetailViewModel(
    val password_id: String,
    val localRepository: LocalRepository,
) : ViewModel() {

    val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception: Throwable ->
        viewModelScope.launch {
            _effect.emit(
                DetailEffect.LoadError(
                    if (exception.message != null)
                        UiText.StaticString(exception.message!!)
                    else
                        UiText.StringResource(Res.string.unknown_error)
                )
            )
        }
    }

    private val _state = MutableStateFlow(DetailState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<DetailEffect>()
    val effect = _effect.asSharedFlow()

    init {
        if (password_id != "-1") {
            loadDetail()
            println("password_id = $password_id")
        } else {
            // New password
            viewModelScope.launch(DispatchersRepository.DispatchersMain + coroutineExceptionHandler) {
                _state.emit(
                    DetailState(
                        passwordItem = PasswordItem(
                            id = "-1",
                            name = "Password",
                            password = "********",
                            saggastion = "Name of dog",
                            note = "I save it!",
                            datetime = "10.12.2020"
                        ),
                        isViewOnly = false
                    )
                )
            }
        }
    }

    fun handleEvent(events: DetailEvent) {
        when (events) {
            DetailEvent.LoadPasswordDetail -> {}
            is DetailEvent.SavePasswordDetail -> {
                saveNewPassword(events.passwordItem)
            }

            is DetailEvent.UpdatePasswordDetail -> {
                _state.update {
                    it.copy(passwordItem = events.passwordItem)
                }
            }

            is DetailEvent.DeleteePasswordDetail -> {
                deletePassword(events.passwordItem)
            }
        }
    }

    private fun deletePassword(passwordItem: PasswordItem) {
        viewModelScope.launch(DispatchersRepository.DispatchersIO + coroutineExceptionHandler) {
            localRepository.deletePassword(passwordItem.mapToEntity())
        }
    }

    private fun saveNewPassword(passwordItem: PasswordItem) {
        viewModelScope.launch(DispatchersRepository.DispatchersIO + coroutineExceptionHandler) {
            if (passwordItem.id != "-1") {
                localRepository.updatePassword(
                    passwordItem.mapToEntity()
                )
            } else {
                val maxId = localRepository.getMaxId()
                val password = passwordItem.copy(id = (maxId.toInt() + 1).toString())
                localRepository.insertPassword(
                    password.mapToEntity()
                )
            }
        }
    }


    fun loadDetail() {

        viewModelScope.launch(coroutineExceptionHandler) {
            _effect.emit(DetailEffect.Loading)

            try {
                val detail = localRepository.getPasswords(password_id)
                _state.emit(
                    DetailState(
                        passwordItem = detail.mapToDomain(),
                        isViewOnly = false
                    )
                )
                _effect.emit(DetailEffect.LoadSuccess)
            } catch (ex: Exception) {
                _effect.emit(
                    DetailEffect.LoadError(
                        UiText.StaticString(ex.message ?: "Unknown Error")
                    )
                )
            }

        }
    }

}