package com.storage.passwords.presentation.passwords

sealed interface PasswordsEvent {
    data object LoadPasswords : PasswordsEvent
    data object LoadPasswordsFromNetwork : PasswordsEvent
    data class NavigateToDetail(val itemId: String) : PasswordsEvent
}
