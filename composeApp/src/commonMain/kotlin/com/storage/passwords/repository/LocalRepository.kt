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

    suspend fun loadData(): Flow<List<PasswordsEntity>> = withContext(Dispatchers.IO) {
        passwordsDao.getAllPasswords()
    }

    suspend fun loadFilteredData(filter: String): Flow<List<PasswordsEntity>> = withContext(Dispatchers.IO) {
        passwordsDao.getFilteredPasswords(filter)
    }

    suspend fun insertPasswords(list: List<PasswordsEntity>) = withContext(Dispatchers.IO) {
        passwordsDao.insertAllPasswords(list)
    }

    suspend fun insertPassword(password: PasswordsEntity) = withContext(Dispatchers.IO) {
        passwordsDao.insertPassword(password)
    }

    suspend fun getCount(): Int = withContext(Dispatchers.IO) { passwordsDao.count() }

    suspend fun getMaxId(): String = withContext(Dispatchers.IO) { passwordsDao.getMaxId() }

    suspend fun getPasswords(id: String): PasswordsEntity {
        return withContext(Dispatchers.IO) {
            passwordsDao.getPasswords(id)
        }
    }
}