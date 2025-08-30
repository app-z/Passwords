package com.storage.passwords.presentation.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.storage.passwords.presentation.menu.navigation.Screen
import com.storage.passwords.presentation.passwords.PasswordsEvent
import com.storage.passwords.presentation.passwords.PasswordsState
import com.storage.passwords.utils.YesNoAlertDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import passwords.composeapp.generated.resources.Detail
import passwords.composeapp.generated.resources.Res
import passwords.composeapp.generated.resources.delete
import passwords.composeapp.generated.resources.delete_message
import passwords.composeapp.generated.resources.save


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun DetailScreen(
    password_id: String,
    onBackHandler: () -> Unit
) {

    val viewModel =
        koinViewModel<DetailViewModel>(
            parameters = {
                parametersOf(password_id)
            }
        )

    val snackbarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()

    val state = viewModel.viewState.collectAsStateWithLifecycle()

    val passwordItem = state.value.passwordItem

    var isConfirmDeleteAlertDialog by remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current
    val effect = viewModel.effect
        .flowWithLifecycle(
            lifecycle = lifecycleOwner.lifecycle,
            minActiveState = Lifecycle.State.STARTED
        )

    LaunchedEffect(key1 = lifecycleOwner.lifecycle) {
        effect.collect {
            when (it) {
                is DetailEffect.LoadError -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            it.message.asStringForSuspend()
                        )
                    }
                }

                is DetailEffect.LoadSuccess -> {
                    println("DetailEffect.LoadSuccess")
                }

                is DetailEffect.Loading -> {
                    println("DetailEffect.Loading")
                }

                is DetailEffect.RequestSuccess -> {
                    scope.launch {
                        snackbarHostState.showSnackbar("Operation completed")
                    }
                    delay(750)
                    onBackHandler.invoke()
                }

                is DetailEffect.DeletePasswordSuccess -> {
                    scope.launch {
                        snackbarHostState.showSnackbar("Delete item completed")
                    }
                    delay(750)
                    onBackHandler.invoke()
                }

                DetailEffect.NavigationBack -> {
                    onBackHandler.invoke()
                }
            }
        }
    }

    BackHandler(enabled = true) {
        println("BackHandler")
        viewModel.setEvent(DetailEvent.NavigationBack)
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.Detail)) },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.setEvent(DetailEvent.NavigationBack)
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

        Column(
            modifier = Modifier
//                .padding(16.dp)
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            DetailScreenImpl(
                password_id = password_id,
                nameInput = passwordItem.name,
                passwordInput = passwordItem.password,
                noteInput = passwordItem.note,
                suggestInput = passwordItem.suggestion,
                onChangeName = {
                    viewModel.setEvent(
                        DetailEvent.UpdatePasswordDetail(
                            passwordItem.copy(name = it)
                        )
                    )
                },
                onChangePassword = {
                    viewModel.setEvent(
                        DetailEvent.UpdatePasswordDetail(
                            passwordItem.copy(password = it)
                        )
                    )
                },
                onChangeNote = {
                    viewModel.setEvent(
                        DetailEvent.UpdatePasswordDetail(
                            passwordItem.copy(note = it)
                        )
                    )
                },
                onChangeSuggast = {
                    viewModel.setEvent(
                        DetailEvent.UpdatePasswordDetail(
                            passwordItem.copy(suggestion = it)
                        )
                    )
                },
                onSaveClick = {
                    viewModel.setEvent(
                        DetailEvent.SavePasswordDetail(passwordItem)
                    )
                },
                onDeleteClick = {
                    isConfirmDeleteAlertDialog = true
                }
            )
        }

        if (isConfirmDeleteAlertDialog) {
            YesNoAlertDialog(
                showDialog = true,
                onConfirm = {
                    isConfirmDeleteAlertDialog = false
                    viewModel.setEvent(
                        DetailEvent.DeleteePasswordDetail(passwordItem)
                    )
                },
                onDismiss = {
                    isConfirmDeleteAlertDialog = false
                },
                title = Res.string.delete,
                message = Res.string.delete_message
            )
        }
    }
}

@Composable
fun DetailScreenImpl(
    nameInput: String,
    passwordInput: String,
    noteInput: String,
    suggestInput: String,
    password_id: String,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onChangeName: (String) -> Unit,
    onChangePassword: (String) -> Unit,
    onChangeNote: (String) -> Unit,
    onChangeSuggast: (String) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            readOnly = false,
            value = nameInput,
            onValueChange = { newText -> onChangeName.invoke(newText) },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            readOnly = false,
            value = passwordInput,
            onValueChange = { newText -> onChangePassword.invoke(newText) },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            readOnly = false,
            value = noteInput,
            onValueChange = { newText -> onChangeNote(newText) },
            label = { Text("Note") },
            modifier = Modifier.fillMaxWidth()
        )
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            readOnly = false,
            value = suggestInput,
            onValueChange = { newText -> onChangeSuggast(newText) },
            label = { Text("Suggest") },
            modifier = Modifier.fillMaxWidth()
        )
    }

    Row(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            modifier = Modifier
                .padding(16.dp),
            onClick = onSaveClick

        ) {
            Text(stringResource(Res.string.save))
        }

        if (password_id != "-1") {
            Button(
                modifier = Modifier
                    .padding(16.dp),
                onClick = onDeleteClick

            ) {
                Text(stringResource(Res.string.delete))
            }
        }
    }

}

@Preview
@Composable
fun PreviewDetailScreenImpl1() {
    DetailScreenImpl(
        nameInput = "Passw",
        passwordInput = "121ewe#@323",
        noteInput = "I save it !!!",
        suggestInput = "What name of you dog?",
        password_id = "",
        {},
        {},
        {},
        {},
        {},
        {}
    )
}

@Preview
@Composable
fun PreviewDetailScreenImpl2() {
    DetailScreenImpl(
        nameInput = "Passw",
        passwordInput = "121ewe#@323",
        noteInput = "I save it !!!",
        suggestInput = "What name of you dog?",
        password_id = "-1",
        {},
        {},
        {},
        {},
        {},
        {}
    )
}