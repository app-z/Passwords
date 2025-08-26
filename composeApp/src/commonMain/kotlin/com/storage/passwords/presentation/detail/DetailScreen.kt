package com.storage.passwords.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.storage.passwords.models.PasswordItem
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import passwords.composeapp.generated.resources.Res
import passwords.composeapp.generated.resources.save


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun DetailScreen(
    onBackHandler: () -> Unit,
    paddingValues: PaddingValues,
    password_id: String,
    onSaveClick: (PasswordItem) -> Unit,
    navController: NavHostController
) {

    val scope = rememberCoroutineScope()

    val viewModel =
        koinViewModel<DetailViewModel>(
            parameters = {
                parametersOf(password_id)
            }
        )

    val state = viewModel.state.collectAsStateWithLifecycle()

    val passwordItem = state.value.passwordItem

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(key1 = lifecycleOwner) {
        viewModel.effect.collect {
            when (it) {
                is DetailEffect.LoadError -> {
                    println("DetailEffect.LoadError")
                }

                DetailEffect.LoadSuccess -> {
                    println("DetailEffect.LoadSuccess")
                }

                DetailEffect.Loading -> {
                    println("DetailEffect.Loading")
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
            .background(Color.White)
            .padding(16.dp)
            .fillMaxSize()
    ) {
        DetailScreenImpl(
            nameInput = passwordItem.name,
            passwordInput = passwordItem.password,
            noteInput = passwordItem.note,
            onChangeName = {
                viewModel.handleEvent(DetailEvent.UpdatePasswordDetail(
                    passwordItem.copy(name = it)
                ))
            },
            onChangePassword = {
                viewModel.handleEvent(DetailEvent.UpdatePasswordDetail(
                    passwordItem.copy(password = it)
                ))
            },
            onChangeNote = {
                viewModel.handleEvent(DetailEvent.UpdatePasswordDetail(
                    passwordItem.copy(note = it)
                ))
            },
            onSaveClick = {
                onSaveClick.invoke(passwordItem)
                viewModel.handleEvent(
                    DetailEvent.SavePasswordDetail(passwordItem)
                )
            }
        )
    }
}

@Composable
fun DetailScreenImpl(
    onSaveClick: () -> Unit,
    nameInput: String,
    passwordInput: String,
    noteInput: String,
    onChangeName: (String) -> Unit,
    onChangePassword : (String) -> Unit,
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

    Button(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        onClick = onSaveClick

    ) {
        Text(stringResource(Res.string.save))
    }

}

@Preview
@Composable
fun PreviewDetailScreenImpl() {
//    DetailScreenImpl(
//        mutableStateOf(DetailState(
//            PasswordItem(
//                name = "Name",
//                password = "Password",
//                note = "I save it"
//            ),
//            isViewOnly = false
//        )),
//        {},
//    )
}