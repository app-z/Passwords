package com.storage.passwords.presentation.menu

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.storage.passwords.presentation.menu.navigation.NavigationViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import passwords.composeapp.generated.resources.Res
import passwords.composeapp.generated.resources.add_password
import passwords.composeapp.generated.resources.edit_password

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BurgerMenu(
    onAddItem: () -> Unit,
    onEditItem: () -> Unit,
    drawerState: DrawerState,
    appTopBarContent: @Composable () -> Unit,
    content: @Composable (paddingValue: PaddingValues) -> Unit
) {

    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = false,
        drawerContent = {
            ModalDrawerSheet {
                Text("Menu", modifier = Modifier.padding(16.dp))
                HorizontalDivider()
                NavigationDrawerItem(
                    label = {
                        Text(text = stringResource(Res.string.add_password))
                    },
                    selected = false,
                    onClick = {
                        onAddItem.invoke()
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = {
                        Text(text = stringResource(Res.string.edit_password))
                    },
                    selected = false,
                    onClick = {
                        onEditItem.invoke()
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = appTopBarContent //{ viewModel.ApplicationTopBar(drawerState = drawerState) }
        ) { paddingValues ->
            // Main screen content
            content(paddingValues)

        }
    }
}
