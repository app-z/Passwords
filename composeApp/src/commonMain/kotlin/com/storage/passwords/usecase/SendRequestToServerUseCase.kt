package com.storage.passwords.usecase

import com.storage.passwords.models.PasswordItem
import com.storage.passwords.repository.NetworkRepository

class SendRequestToServerUseCase(
    private val networkRepository: NetworkRepository
) {

    suspend fun sendPasswordToServer(
        passwordItem: PasswordItem,
        onRequestSuccess: (response: String) -> Unit,
        onLoadError: (error: String?) -> Unit,
    ) {

        val response = networkRepository.savePassword(passwordItem)
        if (response.isSuccess) {
            response.map {
                onRequestSuccess.invoke(it)
            }
        } else {
            onLoadError.invoke(response.exceptionOrNull()?.message)
        }
    }

}