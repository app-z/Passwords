package com.storage.passwords.usecase

import com.storage.passwords.models.PasswordItem
import com.storage.passwords.models.mapToDomain
import com.storage.passwords.models.mapToEntity
import com.storage.passwords.repository.LocalRepository
import com.storage.passwords.repository.NetworkRepository
import kotlinx.coroutines.coroutineScope

class LoadFromInternetUseCase(
    private val networkRepository: NetworkRepository,
    private val localRepository: LocalRepository,
) {

    suspend fun loadFromInternet(
        onLoadSuccess: (passwordItems: List<PasswordItem>) -> Unit,
        onLoadError: (error: String?) -> Unit,
        isFeelDb: Boolean = true
    ) {

        val passwords = networkRepository.getData(0)
        if (passwords.isSuccess) {
            passwords.map { passwordsResults ->
                val passwordItems = passwordsResults.map { passwordResultItem ->
                    passwordResultItem.mapToDomain()
                }
                onLoadSuccess.invoke(passwordItems)

                if (isFeelDb) {
                    localRepository.insertPasswords(
                        passwordItems.map { passwordItem ->
                            passwordItem.mapToEntity()
                        }
                    )
                }
            }
        } else {
            onLoadError.invoke(passwords.exceptionOrNull()?.message)
        }
    }

}