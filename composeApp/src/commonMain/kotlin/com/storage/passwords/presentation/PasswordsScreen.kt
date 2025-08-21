package com.storage.passwords.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.storage.passwords.models.PasswordItem
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PasswordsScreen(
    modifier: Modifier = Modifier
) {
    val viewModel = koinViewModel<PasswordsViewModel>()

    val state by viewModel.state.collectAsStateWithLifecycle()

    val localLifecycleOwner = LocalLifecycleOwner.current

    var successMessage by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val effect = viewModel.effect
        .flowWithLifecycle(
            localLifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )

    LaunchedEffect(key1 = localLifecycleOwner) {
        effect.collect {
            when (it) {
                is PasswordsEffect.LoadError -> {
                    errorMessage = it.message
                }

                PasswordsEffect.LoadSuccess -> {
                    errorMessage = ""
                    successMessage = true
                }
            }
        }
    }

    if (successMessage) {
//        Text("LoadSuccess")
    }

    if (errorMessage.isNotEmpty()) {
        Text(errorMessage)
    }


    when {

        state.passwordItems.isEmpty() -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    text = "Empty"
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    onClick = {
                        viewModel.handleEvent(PasswordsEvent.LoadPasswords)
                    }) {
                    Text("Reload")
                }
            }
        }

        else -> {

            PasswordsListScreen(
                state.passwordItems,
                {
                    viewModel.handleEvent(PasswordsEvent.NavigateToDetail(it.id))
                    println(it)
                })
        }
    }

    ReloadFromNetwork({
        viewModel.handleEvent(PasswordsEvent.LoadPasswordsFromNetwork)
    })
}

@Composable
fun ReloadFromNetwork(onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        onClick = {
            onClick.invoke()
        }) {
        Text("Reload from Internet")
    }
}

@Composable
fun PasswordsListScreen(
    passwordItems: List<PasswordItem>,
    onClickDetail: (passwordsItem: PasswordItem) -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(passwordItems, key = { it.id }) { passwordItem ->
            PasswordRow(
                passwordItem = passwordItem,
                onClick = {
                    onClickDetail.invoke(it)
                }
            )

        }
    }

}

@Composable
fun PasswordRow(
    passwordItem: PasswordItem,
    onClick: (passwordItem: PasswordItem) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick.invoke(passwordItem) }
    ) {
        Text(passwordItem.id)
        Text(passwordItem.name)
    }
}


@Composable
@Preview
fun PasswordRowPreview() {
    PasswordRow(
        PasswordItem(
            id = "100",
            name = "Email",
            password = "safsfdsfsdf",
            saggastion = "What u favorite color?",
            note = "I remember",
            datetime = "10.10.2025"
        ),
        {}
    )
}
