package com.storage.passwords.presentation

import org.koin.core.logger.MESSAGE

sealed interface PasswordsEffect {
    data object LoadSuccess : PasswordsEffect
    data class LoadError(val message: String) : PasswordsEffect
}
