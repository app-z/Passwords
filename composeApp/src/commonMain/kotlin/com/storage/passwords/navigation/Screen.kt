package com.storage.passwords.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Detail : Screen("profile")
    object Settings : Screen("settings")
}