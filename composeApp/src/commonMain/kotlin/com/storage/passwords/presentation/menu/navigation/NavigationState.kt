package com.storage.passwords.presentation.menu.navigation

import com.spacex.utils.UiText

data class NavigationState(
    val route: String = Screen.Home.route,
    val isShowProgress: Boolean = false,
    val error: UiText? = null
)