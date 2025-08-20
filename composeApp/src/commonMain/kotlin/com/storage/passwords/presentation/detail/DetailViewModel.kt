package com.storage.passwords.presentation.detail

import com.spacex.utils.UiText
import com.storage.passwords.models.PasswordItem
import com.storage.passwords.models.mapToDomain
import com.storage.passwords.models.mapToEntity
import com.storage.passwords.repository.DispatchersRepository
import com.storage.passwords.repository.LocalRepository
import com.storage.passwords.usecase.SendRequestToServerUseCase
import com.storage.passwords.utils.BaseViewModel
import kotlinx.coroutines.launch
import passwords.composeapp.generated.resources.Res
import passwords.composeapp.generated.resources.unknown_error
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class DetailViewModel(
    val password_id: String,
    private val localRepository: LocalRepository,
    private val sendRequestToServerUseCase: SendRequestToServerUseCase
) : BaseViewModel<DetailEvent, DetailState, DetailEffect>(DetailState()) {

    override fun onCoroutineException(message: UiText) {
        setEffect { DetailEffect.LoadError(message) }
    }

    override fun runInitialEvent() {
        setEvent(DetailEvent.LoadPasswordDetail)
    }

    init {
        if (password_id != "-1") {
            loadDetail()
            println("password_id = $password_id")
        } else {
            createNewPassword()
        }
    }

    fun createNewPassword() {
        // New password
        defaultViewModelScope.launch(DispatchersRepository.main() + coroutineExceptionHandler) {
            setState {
                DetailState(
                    passwordItem = PasswordItem(
                        id = "-1",
                        name = "Password mail",
                        password = Uuid.random().toString().take(16),
                        suggestion = "What the name of you dog?",
                        note = "I save it in note on 5th page!",
                        datetime = "10.12.2020"
                    ),
                    isViewOnly = false
                )
            }
        }
    }

    override fun handleEvents(event: DetailEvent) {
        when (event) {
            DetailEvent.LoadPasswordDetail -> {}
            is DetailEvent.SavePasswordDetail -> {
                sendPasswordToServer(event.passwordItem)
            }

            is DetailEvent.UpdatePasswordDetail -> {
                setState {
                    copy(passwordItem = event.passwordItem)
                }
            }

            is DetailEvent.DeleteePasswordDetail -> {
                deletePassword(event.passwordItem)
            }

            DetailEvent.NavigationBack -> {
                navigateBack()
            }
        }
    }

    private fun deletePassword(passwordItem: PasswordItem) {
        defaultViewModelScope.launch(DispatchersRepository.io()) {
            localRepository.deletePassword(passwordItem.mapToEntity())
            setEffect {
                DetailEffect.DeletePasswordSuccess
            }
        }
    }

    private fun saveNewPassword(passwordItem: PasswordItem) {
        defaultViewModelScope.launch(DispatchersRepository.io()) {
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

        defaultViewModelScope.launch(coroutineExceptionHandler) {
            setEffect { DetailEffect.Loading }

            try {
                val detail = localRepository.getPasswords(password_id)
                setState {
                    DetailState(
                        passwordItem = detail.mapToDomain(),
                        isViewOnly = false
                    )
                }
                setEffect { DetailEffect.LoadSuccess }
            } catch (ex: Exception) {
                setEffect {
                    DetailEffect.LoadError(
                        if (ex.message != null)
                            UiText.StaticString(ex.message!!)
                        else
                            UiText.StringResource(Res.string.unknown_error)
                    )
                }
            }
        }
    }


    fun sendPasswordToServer(passwordItem: PasswordItem) {
        defaultViewModelScope.launch {
            sendRequestToServerUseCase.sendPasswordToServer(
                passwordItem,
                onRequestSuccess = {
                    saveNewPassword(passwordItem)
                    setEffect { DetailEffect.RequestSuccess }
                },
                onLoadError = {
                    setEffect {
                        DetailEffect.LoadError(
                            if (it.isNullOrEmpty()) {
                                UiText.StringResource(Res.string.unknown_error)
                            } else {
                                UiText.StaticString(it)
                            }
                        )
                    }

                }
            )
        }
    }

    private fun navigateBack() {
        setEffect { DetailEffect.NavigationBack }
    }

}