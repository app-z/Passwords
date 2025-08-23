package com.storage.passwords.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacex.utils.UiText
import com.storage.passwords.models.PasswordItem
import com.storage.passwords.models.mapToDomain
import com.storage.passwords.models.mapToEntity
import com.storage.passwords.repository.LocalRepository
import io.ktor.events.Events
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.handleCoroutineException
import kotlinx.coroutines.launch

class DetailViewModel(
    val password_id: String,
    val localRepository: LocalRepository,
) : ViewModel() {

    val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception: Throwable ->
        viewModelScope.launch {
            _effect.emit(
                DetailEffect.LoadError(
                    UiText.StaticString(exception.message ?: "Unknown Error")
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
        } else {
            viewModelScope.launch(coroutineExceptionHandler) {
                _state.emit(
                    DetailState(
                        passwordItem = PasswordItem(
                            id = "0",
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
            is DetailEvent.AddPasswordDetail -> {
                addNewPassword(events.passwordItem)

            }
        }
    }

    private fun addNewPassword(passwordItem: PasswordItem) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val maxId = localRepository.getMaxId()
            val password = passwordItem.copy(id = (maxId.toInt() + 1).toString())
            localRepository.insertPassword(
                password.mapToEntity()
            )
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