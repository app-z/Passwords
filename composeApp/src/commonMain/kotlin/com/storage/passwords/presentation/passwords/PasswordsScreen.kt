package com.storage.passwords.presentation.passwords

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.spacex.utils.UiText
import com.storage.passwords.models.PasswordItem
import com.storage.passwords.presentation.shimmerEffect
import com.storage.passwords.repository.DispatchersRepository
import com.storage.passwords.utils.ErrorMessageScreen
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PasswordsScreen(
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
    currentItem: (passsword_id: String) -> Unit,
) {
    val viewModel = koinViewModel<PasswordsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val localLifecycleOwner = LocalLifecycleOwner.current

    var errorMessage by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    val effect = viewModel.effect
        .flowWithLifecycle(
            localLifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )

    LaunchedEffect(key1 = localLifecycleOwner) {
        effect.collect {
            when (it) {
                is PasswordsEffect.LoadError -> {
                    errorMessage = it.message.asStringForSuspend()
                }

                PasswordsEffect.LoadSuccess -> {
                    errorMessage = ""
                }

                is PasswordsEffect.NavigateToDetail -> {
                    currentItem.invoke(it.itemId)
                }
            }
        }
    }

    if (errorMessage.isNotEmpty()) {
        scope.launch(DispatchersRepository.main()) {
            snackbarHostState.showSnackbar(errorMessage)
        }
    }


    Column(
        modifier = Modifier
            .background(Color.White)
//            .safeContentPadding()
//            .padding(paddingValues = paddingValues)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        when {

            state.isLoading -> {
                PasswordListShimmer()
            }

            state.passwordItems.isEmpty() -> {
                ErrorMessageScreen(
                    UiText.StaticString("No Data"),
                    onRetry = {
                    viewModel.handleEvent(PasswordsEvent.LoadPasswords)
                })
            }

            else -> {
                PasswordsListScreen(
                    state.passwordItems,
                    {
                        viewModel.handleEvent(PasswordsEvent.NavigateToDetail(it.id))
                        println(it)
                    }
                )
            }
        }
    }
}

@Composable
fun PasswordItemShimmer() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shimmerEffect()
            .padding(16.dp)
    ) {
        Text("Password...")
    }
}

@Composable
fun PasswordListShimmer() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(10) {
            PasswordItemShimmer()
        }
    }
}

@Composable
fun PasswordsListScreen(
    passwordItems: List<PasswordItem>,
    onClickDetail: (passwordsItem: PasswordItem) -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
//            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
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
    modifier: Modifier = Modifier,
    passwordItem: PasswordItem,
    onClick: (passwordItem: PasswordItem) -> Unit
) {

    Card(
        modifier = modifier.background(Color.LightGray)
            .background(Color.White)
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick.invoke(passwordItem) },

        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(start = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .wrapContentWidth(),
                text = passwordItem.id
            )
            Text(
                modifier = Modifier
                    .padding(top = 4.dp, start = 8.dp)
                    .fillMaxWidth(),
                text = passwordItem.name
            )
        }
        Text(
            modifier = Modifier
                .padding(top = 8.dp, start = 8.dp)
                .fillMaxWidth(),
            text = passwordItem.note
        )
    }
}


@Composable
@Preview
fun PasswordRowPreview() {
    PasswordRow(
        modifier = Modifier,
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

@Preview
@Composable
fun PasswordListShimmerPreview() {
    PasswordListShimmer()
}