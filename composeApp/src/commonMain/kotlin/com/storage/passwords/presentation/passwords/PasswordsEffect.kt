package com.storage.passwords.presentation.passwords

import com.spacex.utils.UiText

sealed interface PasswordsEffect {
    data object LoadSuccess : PasswordsEffect
    data class LoadError(val message: UiText) : PasswordsEffect
    data class NavigateToDetail(val itemId: String) : PasswordsEffect

}
