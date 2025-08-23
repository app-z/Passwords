package com.storage.passwords.presentation.detail

import com.storage.passwords.models.PasswordItem

sealed interface DetailEvent {
    data object LoadPasswordDetail : DetailEvent
    data class AddPasswordDetail(val passwordItem: PasswordItem) : DetailEvent

}
