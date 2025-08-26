package com.storage.passwords.repository

import com.storage.passwords.database.PasswordsDao
import com.storage.passwords.models.PasswordsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LocalRepository(
    private val passwordsDao: PasswordsDao
) {

    suspend fun loadData(): Flow<List<PasswordsEntity>> = withContext(DispatchersRepository.io()) {
        passwordsDao.getAllPasswords()
    }

    suspend fun loadFilteredData(filter: String): Flow<List<PasswordsEntity>> = withContext(DispatchersRepository.io()) {
        passwordsDao.getFilteredPasswords(filter)
    }

    suspend fun insertPasswords(list: List<PasswordsEntity>) = withContext(DispatchersRepository.io()) {
        passwordsDao.insertAllPasswords(list)
    }

    suspend fun insertPassword(password: PasswordsEntity) = withContext(DispatchersRepository.io()) {
        passwordsDao.insertPassword(password)
    }

    suspend fun updatePassword(password: PasswordsEntity) = withContext(DispatchersRepository.io()) {
        passwordsDao.updatePassword(password)
    }

    suspend fun deletePassword(password: PasswordsEntity) = withContext(DispatchersRepository.io()) {
        passwordsDao.deletePassword(password)
    }

    suspend fun getCount(): Int = withContext(DispatchersRepository.io()) { passwordsDao.count() }

    suspend fun getMaxId(): String = withContext(DispatchersRepository.io()) { passwordsDao.getMaxId() }

    suspend fun getPasswords(id: String): PasswordsEntity {
        return withContext(DispatchersRepository.io()) {
            passwordsDao.getPasswords(id)
        }
    }
}