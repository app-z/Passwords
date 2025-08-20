package com.storage.passwords.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Passwords")
data class PasswordsEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "note") val note: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "date_utc") var dateUtc: String? = null
)