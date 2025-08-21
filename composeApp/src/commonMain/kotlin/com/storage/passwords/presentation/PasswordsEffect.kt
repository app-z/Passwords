package com.storage.passwords.presentation

import com.spacex.utils.UiText
import org.koin.core.logger.MESSAGE

sealed interface PasswordsEffect {
    data object Loading: PasswordsEffect
    data object LoadSuccess : PasswordsEffect
    data class LoadError(val message: UiText) : PasswordsEffect
}
