package com.storage.passwords.presentation.menu.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import passwords.composeapp.generated.resources.Detail
import passwords.composeapp.generated.resources.Res
import passwords.composeapp.generated.resources.about
import passwords.composeapp.generated.resources.home
import passwords.composeapp.generated.resources.title_settings

sealed class Screen(val route: String) {

    object Home : Screen("home") {
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun MainAppBar(drawerState: DrawerState) {
            val scope = rememberCoroutineScope()
            TopAppBar(
                title = { Text(stringResource(Res.string.home)) },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            if (drawerState.isClosed) drawerState.open() else drawerState.close()
                        }
                    }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                }
            )
        }

    }

    object Detail : Screen("detail") {
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun DetailAppBar(onClickBack: () -> Unit) {
            TopAppBar(
                title = { Text(stringResource(Res.string.Detail)) },
                navigationIcon = {
                    IconButton(onClick = {
                        onClickBack.invoke()
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "ArrowBack"
                        )
                    }
                }
            )
        }
    }

    object About: Screen("About")

    object Settings : Screen("settings") {
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun SettingsAppBar(onClickBack: () -> Unit) {
            TopAppBar(
                title = { Text(stringResource(Res.string.title_settings)) },
                navigationIcon = {
                    IconButton(onClick = {
                        onClickBack.invoke()
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "ArrowBack"
                        )
                    }
                }
            )
        }
    }
}