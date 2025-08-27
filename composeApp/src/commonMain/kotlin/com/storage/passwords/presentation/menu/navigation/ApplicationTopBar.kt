package com.storage.passwords.presentation.menu.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ApplicationTopBar(
    navController: NavController,
    drawerState: DrawerState
) {

    val navigateViewModel = koinViewModel<NavigationViewModel>(
        parameters = { parametersOf(navController) }
    )


    val route = navigateViewModel.currentRoute.collectAsStateWithLifecycle()

    when (route.value) {
        Screen.Home.route -> Screen.Home.MainAppBar(drawerState)
        Screen.About.route -> {}
        Screen.Detail.route -> Screen.Detail.DetailAppBar {
            navigateViewModel.popBackStackToHome()
        }

        Screen.Settings.route -> Screen.Settings.SettingsAppBar {
            navigateViewModel.popBackStackToHome()
        }
    }
}
