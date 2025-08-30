package com.storage.passwords.presentation.menu.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.storage.passwords.presentation.about.AboutScreen
import com.storage.passwords.presentation.detail.DetailScreen
import com.storage.passwords.presentation.menu.BurgerMenu
import com.storage.passwords.presentation.passwords.PasswordsScreen
import com.storage.passwords.presentation.settings.SettingsScreen
import com.storage.passwords.presentation.settings.SettingsViewModel
import com.storage.passwords.utils.Const.PASSWORD_ID_PARAM
import kotlinx.serialization.json.Json
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun NavigationApplication(
    settingViewModel: SettingsViewModel,
) {

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    BurgerMenu(
        drawerState = drawerState,
        onAddItem = {
            navController.currentBackStackEntry?.savedStateHandle?.apply {
                val jsonFalconInfo = Json.encodeToString("-1")
                set(PASSWORD_ID_PARAM, jsonFalconInfo)
            }
            navController.navigate(Screen.Detail.route)
        },
        onAboutItem = {
            navController.navigate(Screen.About.route)
        },
        onSettingsItem = {
            navController.navigate(Screen.Settings.route)
        }
    ) {


        NavHost(
            navController = navController,
            startDestination = Screen.Home.route
        ) {
            composable(Screen.Home.route) {
                PasswordsScreen(
                    drawerState = drawerState,
                    currentItem = { password_id ->
                        navController.currentBackStackEntry?.savedStateHandle?.apply {
                            val jsonFalconInfo = Json.encodeToString(password_id)
                            set(PASSWORD_ID_PARAM, jsonFalconInfo)
                        }
                        navController.navigate(Screen.Detail.route)
                    }

                )
            }
            composable(Screen.About.route) {
                AboutScreen(
                    onStartClick = {
                        navController.popBackStack()
                    }
                )
            }
            composable(
                route = Screen.Detail.route,
            ) {
                navController.previousBackStackEntry?.savedStateHandle?.get<String>(PASSWORD_ID_PARAM)
                    ?.let { jsonId ->
                        val password_id = Json.decodeFromString<String>(jsonId)
                        DetailScreen(
                            password_id = password_id,
                            onBackHandler = {
                                navController.popBackStack()
                            }
                        )
                    }
            }
            composable(Screen.Settings.route) {
                SettingsScreen(
                    viewModel = settingViewModel,
                    {
                        navController.popBackStack()
                    }
                )
            }

        }
    }
}

