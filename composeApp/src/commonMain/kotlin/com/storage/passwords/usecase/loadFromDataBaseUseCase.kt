package com.storage.passwords.usecase

import com.storage.passwords.models.PasswordItem
import com.storage.passwords.models.mapToDomain
import com.storage.passwords.repository.LocalRepository
import kotlinx.coroutines.flow.catch

class LoadFromDataBaseUseCase(
    private val localRepository: LocalRepository
) {

    suspend fun loadFromDataBase(
        onLoadSuccess: (passwordItems: List<PasswordItem>) -> Unit,
        onLoadError: (error: String?) -> Unit,
    ) {
        localRepository
            .loadData()
            .catch {
                onLoadError(it.message)
            }
            .collect { passwordsEntities ->
                onLoadSuccess(passwordsEntities.map { it.mapToDomain() })
            }

    }
}