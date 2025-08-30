package com.storage.passwords.presentation.settings

import com.storage.passwords.utils.Const
import com.storage.passwords.utils.ViewState

data class SettingsState(
    val currentTheme: String = Const.Theme.DARK_MODE.name,
) : ViewState
