package com.storage.passwords.presentation.settings

import com.spacex.utils.UiText
import com.storage.passwords.utils.ViewSideEffect

sealed interface SettingsEffect : ViewSideEffect {
    data object NavigationBack: SettingsEffect

    data class CoroutineError(val message: UiText) : SettingsEffect

}
