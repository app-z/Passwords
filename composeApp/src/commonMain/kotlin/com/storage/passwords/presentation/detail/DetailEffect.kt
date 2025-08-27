package com.storage.passwords.presentation.detail

import com.spacex.utils.UiText

sealed interface DetailEffect {
    data object Loading: DetailEffect
    data object LoadSuccess : DetailEffect
    data object RequestSuccess : DetailEffect
    data class LoadError(val message: UiText) : DetailEffect

    data object DeletePasswordSuccess: DetailEffect
}
