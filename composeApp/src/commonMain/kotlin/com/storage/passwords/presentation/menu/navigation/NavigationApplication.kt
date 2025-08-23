package com.storage.passwords.presentation.menu.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.storage.passwords.presentation.detail.DetailScreen
import com.storage.passwords.presentation.menu.BurgerMenu
import com.storage.passwords.presentation.passwords.PasswordsScreen
import kotlinx.serialization.json.Json
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun NavigationApplication() {

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val navigateViewModel = koinViewModel<NavigationViewModel>(
        parameters = { parametersOf(navController) }
    )

    BurgerMenu(
        drawerState = drawerState,
        onAddItem = {
            navigateViewModel.navigateToRoute(Screen.Detail.route)
        },
        onEditItem = {
            navController.currentBackStackEntry?.savedStateHandle?.apply {
                val jsonFalconInfo = Json.encodeToString("1")
                set("password_id", jsonFalconInfo)
            }
            navigateViewModel.navigateToRoute(Screen.Detail.route)
        },
        appTopBarContent = {
            ApplicationTopBar(
                navigateViewModel = navigateViewModel,
                drawerState = drawerState
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
                    paddingValues = paddingValues,
                    currentItem = { password_id ->
                        navController.currentBackStackEntry?.savedStateHandle?.apply {
                            val jsonFalconInfo = Json.encodeToString(password_id)
                            set("password_id", jsonFalconInfo)
                        }
                        navigateViewModel.navigateToRoute(Screen.Detail.route)
                    }

                )
            }
            composable(
                route = Screen.Detail.route,
            ) {
                navController.previousBackStackEntry?.savedStateHandle?.get<String>("password_id")
                    ?.let { jsonId ->
                        val password_id = Json.decodeFromString<String>(jsonId)
                        DetailScreen(
                            onBackHandler = {
                                navigateViewModel.popBackStackToHome()
                            },
                            password_id = password_id,
                            paddingValues = paddingValues,
                            navController = navController
                        )
                    }
            }

        }
    }
}


@Composable
fun ApplicationTopBar(
    navigateViewModel: NavigationViewModel,
    drawerState: DrawerState
) {

    val route = navigateViewModel.currentRoute.collectAsStateWithLifecycle()

    when (route.value) {
        Screen.Home.route -> Screen.Home.MainAppBar(drawerState)
        Screen.Detail.route -> Screen.Detail.DetailAppBar(
            onClickBack = {
                navigateViewModel.popBackStackToHome()
            }
        )

        Screen.Settings.route -> {}
    }
}
