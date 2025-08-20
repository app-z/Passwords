package com.storage.passwords.presentation.passwords

import com.storage.passwords.models.PasswordItem

data class PasswordsState(
    val passwordItems : List<PasswordItem> = emptyList(),
    var isLoading: Boolean = false
)
