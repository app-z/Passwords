package com.storage.passwords.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.storage.passwords.utils.Const
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import passwords.composeapp.generated.resources.Res
import passwords.composeapp.generated.resources.title_settings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    rootNavController: NavController,
    snackBarHostState: SnackbarHostState,
    paddingValues: PaddingValues,
    viewModel: SettingsViewModel
) {

    val state = viewModel.state.collectAsStateWithLifecycle()

    SettingScreenContent(
        paddingValues = paddingValues,
        state = state,
        onChangeTheme = { themeDark ->
            viewModel.handleEvent(
                SettingsEvent.Theme(
                    if (themeDark) {
                        Const.Theme.DARK_MODE.name
                    } else {
                        Const.Theme.LIGHT_MODE.name
                    }
                )
            )
        }
    )
}


@Composable
fun SettingScreenContent(
    paddingValues: PaddingValues,
    state: State<SettingsState>,
    onChangeTheme: (themeDark: Boolean) -> Unit
) {
    Column(
        modifier = Modifier.padding(8.dp)
            .fillMaxWidth()
    ) {

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                modifier = Modifier.padding(16.dp)
                    .align(Alignment.CenterVertically)
                    .weight(1f),
                text = "Dark Theme"
            )

            Checkbox(
                checked = state.value.currentTheme == Const.Theme.DARK_MODE.name,
                onCheckedChange = {
                    onChangeTheme.invoke(it)
                },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}


@Preview
@Composable
fun PreviewSettings1() {
    SettingScreenContent(
        paddingValues = PaddingValues(),
        state = mutableStateOf<SettingsState>(
            SettingsState(
                currentTheme = Const.Theme.DARK_MODE.name,
            )
        ),
        onChangeTheme = {}
    )
}

@Preview
@Composable
fun PreviewSettings2() {
    SettingScreenContent(
        paddingValues = PaddingValues(),
        state = mutableStateOf<SettingsState>(
            SettingsState(
                currentTheme = Const.Theme.LIGHT_MODE.name,
            )
        ),
        onChangeTheme = {}
    )
}
