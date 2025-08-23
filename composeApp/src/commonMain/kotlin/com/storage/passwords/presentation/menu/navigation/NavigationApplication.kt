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
import com.storage.passwords.presentation.about.AboutScreen
import com.storage.passwords.presentation.detail.DetailScreen
import com.storage.passwords.presentation.menu.BurgerMenu
import com.storage.passwords.presentation.passwords.PasswordsScreen
import kotlinx.serialization.json.Json
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import com.storage.passwords.utils.Const.PASSWORD_ID_PARAM
import passwords.composeapp.generated.resources.Res

import io.github.kdroidfilter.knotify.builder.ExperimentalNotificationsApi
import io.github.kdroidfilter.knotify.builder.notification
import org.jetbrains.compose.resources.ExperimentalResourceApi
import passwords.composeapp.generated.resources.ic_notification_add

@OptIn(ExperimentalResourceApi::class, ExperimentalNotificationsApi::class)
@Composable
fun NavigationApplication() {

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val navigateViewModel = koinViewModel<NavigationViewModel>(
        parameters = { parametersOf(navController) }
    )

    // Create a notification
    val myNotification = notification(
        title = "Notification from Screen 1",
        message = "This is a test notification from Screen 1",
        largeIcon = Res.getUri("drawable/ic_notification_add.png"),
        smallIcon = Res.getUri("drawable/ic_notification_add.png"),
        onActivated = {
            println( "Notification 1 activated")
        },
        onDismissed = {
                reason ->
            println("Notification 1 dismissed: $reason")
        },
        onFailed = {
            println( "Notification 1 failed")
        }
    ) {
        button(title = "Show Message from Button 1") {
            println("Button 1 from Screen 1 clicked")
//            onShowMessage("Button 1 clicked from Screen 1's notification")
        }
        button(title = "Hide Message from Button 2") {
            println("Button 2 from Screen 1 clicked")
//            onShowMessage(null)
        }
    }

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
                                myNotification.send()
                            },
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
        Screen.About.route -> {}
        Screen.Detail.route -> Screen.Detail.DetailAppBar(
            onClickBack = {
                navigateViewModel.popBackStackToHome()
            }
        )

        Screen.Settings.route -> {}
    }
}
