package com.storage.passwords.models

data class PasswordItem(
    val id: String = "-1",
    val name: String = "",
    val password: String = "",
    val suggestion: String = "",
    val note: String = "",
    val datetime: String = ""
)