package com.storage.passwords.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.storage.passwords.utils.Const
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import passwords.composeapp.generated.resources.Res
import passwords.composeapp.generated.resources.title_settings

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigationBack: () -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()

    val state = viewModel.viewState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current

    val effect = viewModel.effect.flowWithLifecycle(
        lifecycle = lifecycleOwner.lifecycle,
        minActiveState = Lifecycle.State.STARTED
    )

    LaunchedEffect(key1 = lifecycleOwner.lifecycle) {
        effect.collect {
            when (it) {
                SettingsEffect.NavigationBack -> onNavigationBack()
                is SettingsEffect.CoroutineError -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            it.message.asStringForSuspend()
                        )
                    }
                }
            }
        }
    }


//    BackHandler(enabled = true) {
//        println("BackHandler")
//        viewModel.handleEvents(SettingsEvent.NavigationBack)
//    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.title_settings)) },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.handleEvents(SettingsEvent.NavigationBack)
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "ArrowBack"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        SettingScreenContent(
            paddingValues = paddingValues,
            state = state,
            onChangeTheme = { themeDark ->
                viewModel.handleEvents(
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
}


@Composable
fun SettingScreenContent(
    paddingValues: PaddingValues,
    state: State<SettingsState>,
    onChangeTheme: (themeDark: Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
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
