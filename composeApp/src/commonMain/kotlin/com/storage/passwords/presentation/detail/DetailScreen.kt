package com.storage.passwords.presentation.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.storage.passwords.utils.YesNoAlertDialog
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import passwords.composeapp.generated.resources.Res
import passwords.composeapp.generated.resources.delete
import passwords.composeapp.generated.resources.delete_message
import passwords.composeapp.generated.resources.save


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun DetailScreen(
    paddingValues: PaddingValues,
    password_id: String,
    snackbarHostState: SnackbarHostState,
    onBackHandler: () -> Unit
) {

    val viewModel =
        koinViewModel<DetailViewModel>(
            parameters = {
                parametersOf(password_id)
            }
        )

    val state = viewModel.state.collectAsStateWithLifecycle()

    val passwordItem = state.value.passwordItem

    var isConfirmDeleteAlertDialog by remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(key1 = lifecycleOwner) {
        viewModel.effect.collect {
            when (it) {
                is DetailEffect.LoadError -> {
                    snackbarHostState.showSnackbar(
                        it.message.asStringForSuspend()
                    )
                    println("DetailEffect.LoadError")
                }

                is DetailEffect.LoadSuccess -> {
                    println("DetailEffect.LoadSuccess")
                }

                is DetailEffect.Loading -> {
                    println("DetailEffect.Loading")
                }

                is DetailEffect.RequestSuccess -> {
                    snackbarHostState.showSnackbar("RequestSuccess")
                    println("DetailEffect.RequestSuccess")
                }

                is DetailEffect.DeletePasswordSuccess -> {
                    onBackHandler.invoke()
                }
            }
        }
    }

    BackHandler(enabled = true) {
        println("BackHandler")
        onBackHandler.invoke()
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        DetailScreenImpl(
            password_id = password_id,
            nameInput = passwordItem.name,
            passwordInput = passwordItem.password,
            noteInput = passwordItem.note,
            onChangeName = {
                viewModel.handleEvent(
                    DetailEvent.UpdatePasswordDetail(
                        passwordItem.copy(name = it)
                    )
                )
            },
            onChangePassword = {
                viewModel.handleEvent(
                    DetailEvent.UpdatePasswordDetail(
                        passwordItem.copy(password = it)
                    )
                )
            },
            onChangeNote = {
                viewModel.handleEvent(
                    DetailEvent.UpdatePasswordDetail(
                        passwordItem.copy(note = it)
                    )
                )
            },
            onSaveClick = {
                viewModel.handleEvent(
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
                viewModel.handleEvent(
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

@Composable
fun DetailScreenImpl(
    nameInput: String,
    passwordInput: String,
    noteInput: String,
    password_id: String,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onChangeName: (String) -> Unit,
    onChangePassword: (String) -> Unit,
    onChangeNote: (String) -> Unit
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
fun PreviewDetailScreenImpl() {
    DetailScreenImpl(
        nameInput = "Passw",
        passwordInput = "121ewe#@323",
        noteInput = "I save it !!!",
        password_id = "",
        {},
        {},
        {},
        {},
        {},
    )
}