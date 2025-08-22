package com.storage.passwords.presentation.menu

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
import com.storage.passwords.navigation.Screen
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import passwords.composeapp.generated.resources.Detail
import passwords.composeapp.generated.resources.Res
import passwords.composeapp.generated.resources.home

@Composable
fun ApplicationTopBar(
    drawerState: DrawerState,
    navController: NavController,
    currentRoute: MutableState<String>) {


    when (currentRoute.value) {
        Screen.Home.route -> MainAppBar(drawerState)
        Screen.Detail.route -> DetailAppBar(navController = navController, topBarItem = currentRoute)
        Screen.Settings.route -> {}
    }

}


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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailAppBar(navController: NavController,
                 topBarItem: MutableState<String>) {
    val scope = rememberCoroutineScope()

    TopAppBar(
        title = { Text(stringResource(Res.string.Detail)) },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    navController.navigateUp()
                    topBarItem.value = Screen.Home.route
                }
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    )
}

