package com.storage.passwords.presentation.settings

sealed interface SettingsEvent {
    data class Theme(val currentThene: String) : SettingsEvent
}
