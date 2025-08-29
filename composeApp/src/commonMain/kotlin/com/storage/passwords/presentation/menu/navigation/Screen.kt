package com.storage.passwords.presentation.menu.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import passwords.composeapp.generated.resources.Detail
import passwords.composeapp.generated.resources.Res
import passwords.composeapp.generated.resources.home
import passwords.composeapp.generated.resources.title_settings

sealed class Screen(val route: String) {

    object Home : Screen("home")
    object Detail : Screen("detail")
    object About : Screen("about")
    object Settings : Screen("settings")
}