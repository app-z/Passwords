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
import passwords.composeapp.generated.resources.about
import passwords.composeapp.generated.resources.add_password
import passwords.composeapp.generated.resources.edit_password

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BurgerMenu(
    onAboutItem: () -> Unit,
    onAddItem: () -> Unit,
    drawerState: DrawerState,
    content: @Composable () -> Unit
) {

    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet {
                Text("Menu", modifier = Modifier.padding(16.dp))
                HorizontalDivider()
                NavigationDrawerItem(
                    label = {
                        Text(text = stringResource(Res.string.about))
                    },
                    selected = false,
                    onClick = {
                        onAboutItem.invoke()
                        scope.launch { drawerState.close() }
                    }
                )
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
            }
        }
    ) {
            // Main screen content
            content()
    }
}
