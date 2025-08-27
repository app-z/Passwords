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
fun NavigationApplication(settingViewModel: SettingsViewModel) {

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val navigateViewModel = koinViewModel<NavigationViewModel>(
        parameters = { parametersOf(navController) }
    )

    val snackbarHostState = remember { SnackbarHostState() }

    BurgerMenu(
        drawerState = drawerState,
        onAddItem = {
            navController.currentBackStackEntry?.savedStateHandle?.apply {
                val jsonFalconInfo = Json.encodeToString("-1")
                set(PASSWORD_ID_PARAM, jsonFalconInfo)
            }
            navigateViewModel.navigateToRoute(Screen.Detail.route)
        },
        onAboutItem = {
            navigateViewModel.navigateToRoute(Screen.About.route)
        },
        onReloadItem = {
            navigateViewModel.reloadPasswords()
        },
        onSettingsItem = {
            navigateViewModel.navigateToRoute(Screen.Settings.route)
        }
    ) {

        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            topBar = {
                ApplicationTopBar(
                    navController = navController,
                    drawerState = drawerState
                )
            },
            bottomBar = {
                ApplicationBottomBar(
                    navController = navController,
                    drawerState = drawerState,
                    onReload = {
                        navigateViewModel.reloadPasswords()
                    }
                )
            }
        ) { paddingValues ->

            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(Screen.Home.route) {
                    PasswordsScreen(
                        snackbarHostState = snackbarHostState,
                        paddingValues = paddingValues,
                        currentItem = { password_id ->
                            navController.currentBackStackEntry?.savedStateHandle?.apply {
                                val jsonFalconInfo = Json.encodeToString(password_id)
                                set(PASSWORD_ID_PARAM, jsonFalconInfo)
                            }
                            navigateViewModel.navigateToRoute(Screen.Detail.route)
                        }

                    )
                }
                composable(Screen.About.route) {
                    AboutScreen(
                        paddingValues = paddingValues,
                        onStartClick = {
                            navigateViewModel.popBackStackToHome()
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
                                onBackHandler = {
                                    navigateViewModel.popBackStackToHome()
                                },
                                password_id = password_id,
                                paddingValues = paddingValues,
                                onSaveClick = {
                                    navigateViewModel.popBackStackToHome()
                                },
                                onDeleteClick = {
                                    navigateViewModel.popBackStackToHome()
                                }
                            )
                        }
                }
                composable(Screen.Settings.route) {
                    SettingsScreen(
                        paddingValues = paddingValues,
                        rootNavController = navController,
                        snackBarHostState = snackbarHostState,
                        viewModel = settingViewModel
                    )
                }

            }
        }
    }
}

