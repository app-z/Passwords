package com.storage.passwords.repository

import com.storage.passwords.database.PasswordsDao
import com.storage.passwords.models.PasswordsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LocalRepository(
    private val passwordsDao: PasswordsDao
) {

    suspend fun loadData(): Flow<List<PasswordsEntity>> = withContext(DispatchersRepository.DispatchersIO) {
        passwordsDao.getAllPasswords()
    }

    suspend fun loadFilteredData(filter: String): Flow<List<PasswordsEntity>> = withContext(DispatchersRepository.DispatchersIO) {
        passwordsDao.getFilteredPasswords(filter)
    }

    suspend fun insertPasswords(list: List<PasswordsEntity>) = withContext(DispatchersRepository.DispatchersIO) {
        passwordsDao.insertAllPasswords(list)
    }

    suspend fun insertPassword(password: PasswordsEntity) = withContext(DispatchersRepository.DispatchersIO) {
        passwordsDao.insertPassword(password)
    }

    suspend fun updatePassword(password: PasswordsEntity) = withContext(DispatchersRepository.DispatchersIO) {
        passwordsDao.updatePassword(password)
    }

    suspend fun deletePassword(password: PasswordsEntity) = withContext(DispatchersRepository.DispatchersIO) {
        passwordsDao.deletePassword(password)
    }

    suspend fun getCount(): Int = withContext(DispatchersRepository.DispatchersIO) { passwordsDao.count() }

    suspend fun getMaxId(): String = withContext(DispatchersRepository.DispatchersIO) { passwordsDao.getMaxId() }

    suspend fun getPasswords(id: String): PasswordsEntity {
        return withContext(DispatchersRepository.DispatchersIO) {
            passwordsDao.getPasswords(id)
        }
    }
}