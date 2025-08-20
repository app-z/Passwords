package com.storage.passwords.presentation.settings

import com.storage.passwords.utils.Const

data class SettingsState(
    val currentTheme: String = Const.Theme.DARK_MODE.name,
)
