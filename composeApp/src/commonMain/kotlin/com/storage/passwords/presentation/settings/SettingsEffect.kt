package com.storage.passwords.presentation.settings

import com.storage.passwords.utils.ViewSideEffect

sealed interface SettingsEffect : ViewSideEffect {
    data object NavigationBack: SettingsEffect
}
