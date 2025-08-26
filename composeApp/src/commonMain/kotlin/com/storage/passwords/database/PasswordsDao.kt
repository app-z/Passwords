package com.storage.passwords.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.storage.passwords.models.PasswordsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordsDao {

    @Query("SELECT * FROM Passwords ORDER BY id")
    fun getAllPasswords(): Flow<List<PasswordsEntity>>

    @Query("SELECT * FROM Passwords WHERE name LIKE '%' || :filter || '%' ORDER BY id")
    fun getFilteredPasswords(filter: String): Flow<List<PasswordsEntity>>

    @Query("SELECT * FROM Passwords WHERE id = :id")
    suspend fun getPasswords(id: String): PasswordsEntity

    @Query("SELECT COUNT(*) as count FROM Passwords")
    suspend fun count(): Int

    @Query("SELECT id FROM Passwords ORDER BY id DESC")
    suspend fun getMaxId(): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPasswords(passwords: List<PasswordsEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertPassword(password: PasswordsEntity)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updatePassword(password: PasswordsEntity)


}
