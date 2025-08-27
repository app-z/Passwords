package com.storage.passwords.models


fun PasswordsResult.mapToDomain() = PasswordItem(
    id = id,
    name = name,
    password = password,
    note = note,
    datetime = dateUtc ?: "No Date"
//val saggastion: String = "",
)

fun PasswordItem.mapToRequest() = PasswordsRequest(
    id = id,
    name = name,
    password = password,
    note = note
)

fun PasswordItem.mapToEntity() = PasswordsEntity(
    id = id,
    name = name,
    note = note,
    password = password,
    dateUtc = datetime
)

fun PasswordsEntity.mapToDomain() = PasswordItem(
    id = id,
    name = name,
    note = note,
    password = password,
    datetime = dateUtc ?: "No Date"
)