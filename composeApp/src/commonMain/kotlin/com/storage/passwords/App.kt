package com.storage.passwords

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.storage.passwords.navigation.Screen
import com.storage.passwords.presentation.detail.DetailScreen
import com.storage.passwords.presentation.menu.BurgerMenu
import com.storage.passwords.presentation.menu.ApplicationTopBar
import com.storage.passwords.presentation.passwords.PasswordsScreen
import kotlinx.serialization.json.Json
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {

    val onAddItem: () -> Unit = {}
    val onEditItem: () -> Unit = {}

    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val currentRoute = remember { mutableStateOf(Screen.Home.route) }

    MaterialTheme {

        BurgerMenu(
            topBar = {
                ApplicationTopBar(
                    navController = navController,
                    currentRoute = currentRoute,
                    drawerState = drawerState
                )
            },
            drawerState = drawerState,
            onAddItem = {
            },
            onEditItem = {
                navController.currentBackStackEntry?.savedStateHandle?.apply {
                    val jsonFalconInfo = Json.encodeToString("1")
                    set("password_id", jsonFalconInfo)
                }
                navController.navigate(Screen.Detail.route)
                currentRoute.value = Screen.Detail.route
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
                            navController.navigate(Screen.Detail.route)
                            currentRoute.value = Screen.Detail.route
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
                                    navController.popBackStack()
                                    currentRoute.value = Screen.Home.route
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

}

