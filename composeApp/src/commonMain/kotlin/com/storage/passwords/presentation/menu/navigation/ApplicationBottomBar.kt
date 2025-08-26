package com.storage.passwords.presentation.menu.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ApplicationBottomBar(
    navController: NavController,
    drawerState: DrawerState,
    onReload: () -> Unit
) {

    val navigateViewModel = koinViewModel<NavigationViewModel>(
        parameters = { parametersOf(navController) }
    )


    val route = navigateViewModel.currentRoute.collectAsStateWithLifecycle()

    when (route.value) {
        Screen.Home.route -> {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = onReload) {
                    Text("Reload from Internet")
                }
            }
        }
        Screen.About.route -> {}
        Screen.Detail.route -> {}

        Screen.Settings.route -> {}
    }
}
