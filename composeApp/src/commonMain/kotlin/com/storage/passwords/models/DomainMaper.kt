package com.storage.passwords.models


fun PasswordsResult.mapToDomain() = PasswordItem(
    id = id,
    name = name,
    password = password,
    note = note,
    datetime = dateUtc ?: "No Date"
//val saggastion: String = "",
//val note: String = "",
)

fun PasswordItem.mapToEntity() = PasswordsEntity(
    id = id,
    name = name,
    note = note,
    password = password,
    dateUtc = datetime
)