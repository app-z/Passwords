package com.storage.passwords.presentation.settings

import com.storage.passwords.utils.ViewEvent

sealed interface SettingsEvent : ViewEvent {
    data class Theme(val currentThene: String) : SettingsEvent
    data object NavigationBack: SettingsEvent
}
