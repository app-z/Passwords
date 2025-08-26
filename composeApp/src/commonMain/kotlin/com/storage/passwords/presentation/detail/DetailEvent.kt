package com.storage.passwords.presentation.detail

import com.storage.passwords.models.PasswordItem

sealed interface DetailEvent {
    data object LoadPasswordDetail : DetailEvent
    data class SavePasswordDetail(val passwordItem: PasswordItem) : DetailEvent

    data class UpdatePasswordDetail(val passwordItem: PasswordItem) : DetailEvent
    data class DeleteePasswordDetail(val passwordItem: PasswordItem) : DetailEvent

}
