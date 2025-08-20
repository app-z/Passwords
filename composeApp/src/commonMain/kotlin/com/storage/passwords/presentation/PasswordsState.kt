package com.storage.passwords.presentation

import com.storage.passwords.models.PasswordItem

data class PasswordsState(
    val passwordItems : List<PasswordItem> = emptyList(),
)
