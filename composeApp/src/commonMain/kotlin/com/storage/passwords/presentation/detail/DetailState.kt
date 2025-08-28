package com.storage.passwords.presentation.detail

import com.storage.passwords.models.PasswordItem
import com.storage.passwords.utils.ViewState

data class DetailState (
    val passwordItem : PasswordItem = PasswordItem(),
    val isViewOnly: Boolean = false
) : ViewState
