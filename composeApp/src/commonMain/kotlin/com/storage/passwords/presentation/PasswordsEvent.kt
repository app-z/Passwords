package com.storage.passwords.presentation

sealed interface PasswordsEvent {
    data object LoadPasswords : PasswordsEvent
    data object LoadPasswordsFromNetwork : PasswordsEvent
    data class NavigateToDetail(val itemId: String) : PasswordsEvent
}
