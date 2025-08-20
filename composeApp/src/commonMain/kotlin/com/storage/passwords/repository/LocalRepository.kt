package com.storage.passwords.repository

import com.storage.passwords.database.PasswordsDao
import com.storage.passwords.models.PasswordsEntity
import kotlinx.coroutines.flow.Flow

class LocalRepository(
    private val passwordsDao: PasswordsDao
) {

    fun loadData(): Flow<List<PasswordsEntity>> =
        passwordsDao.getAllPasswords()

    fun loadFilteredData(filter: String): Flow<List<PasswordsEntity>> =
        passwordsDao.getFilteredPasswords(filter)

    suspend fun insertPasswords(list: List<PasswordsEntity>) =
        passwordsDao.insertAllPasswords(list)

    suspend fun getCount(): Int = passwordsDao.count()

    suspend fun getPasswords(id: String): PasswordsEntity {
        val passwords = passwordsDao.getPasswords(id)
        return passwords
    }
}