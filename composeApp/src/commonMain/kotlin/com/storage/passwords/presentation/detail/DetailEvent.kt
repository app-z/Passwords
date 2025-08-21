package com.storage.passwords.presentation.detail

sealed interface DetailEvent {
    data object LoadPasswordDetail : DetailEvent
}
