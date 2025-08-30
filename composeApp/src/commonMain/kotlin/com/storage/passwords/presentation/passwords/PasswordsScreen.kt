package com.storage.passwords.presentation.passwords

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import passwords.composeapp.generated.resources.Res
import passwords.composeapp.generated.resources.home

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordsScreen(
    drawerState: DrawerState,
    currentItem: (passsword_id: String) -> Unit,
) {
    val viewModel = koinViewModel<PasswordsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val localLifecycleOwner = LocalLifecycleOwner.current

    val scope = rememberCoroutineScope()

    val effect = viewModel.effect
        .flowWithLifecycle(
            localLifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )

    LaunchedEffect(key1 = localLifecycleOwner.lifecycle) {
        effect.collect {
            when (it) {
                is PasswordsEffect.LoadError -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(it.message.asStringForSuspend())
                    }
                }

                PasswordsEffect.LoadSuccess -> {
                    scope.launch {
                        snackbarHostState.showSnackbar("Loading items was successful")
                    }
                }

                is PasswordsEffect.NavigateToDetail -> {
                    currentItem.invoke(it.itemId)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.home)) },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            if (drawerState.isClosed) drawerState.open() else drawerState.close()
                        }
                    }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    viewModel.handleEvent(PasswordsEvent.LoadPasswordsFromNetwork)
                }) {
                    Text("Reload from Internet")
                }
            }
        }
    ) { paddingValues ->


        Column(
            modifier = Modifier
//            .safeContentPadding()
                .padding(paddingValues = paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            when {

                state.isLoading -> {
                    PasswordListShimmer()
                }

                state.passwordItems.isEmpty() -> {
                    ErrorMessageScreen(
                        error = UiText.StaticString("No Data"),
                        onRetry = {
                            viewModel.handleEvent(PasswordsEvent.LoadPasswords)
                        })
                }

                else -> {
                    PasswordsListScreen(
                        state.passwordItems,
                        onClickDetail = {
                            viewModel.handleEvent(PasswordsEvent.NavigateToDetail(it.id))
                            println(it)
                        }
                    )
                }
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

    var showPassword by remember { mutableStateOf(false) }

    var timeLeft by remember { mutableStateOf(0) }

    val navAllow = derivedStateOf { !showPassword }

    LaunchedEffect(key1 = navAllow.value) {
        while (timeLeft > 0) {
            delay(100)
            timeLeft--
        }
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(120.dp),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
                .clickable {
                    if (timeLeft <= 0) {
                        onClick.invoke(passwordItem)
                    }
                },
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                modifier = Modifier
                    .weight(0.9f)
            ) {

                Row(
                    modifier = Modifier
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .padding(top = 4.dp),
                        text = passwordItem.id
                    )
                    Text(
                        modifier = Modifier
                            .padding(top = 4.dp, start = 24.dp),
                        text = if (showPassword) passwordItem.password else "*********"
                    )
                }
                Text(
                    modifier = Modifier
                        .padding(8.dp),
                    text = passwordItem.note
                )
            }


            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 8.dp)
                    .weight(0.1f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.pointerInput(Unit) {
                        awaitEachGesture {
                            val down = awaitFirstDown()
                            // Handle the down event
                            showPassword = true

                            do {
                                val event = awaitPointerEvent()
                            } while (event.changes.any { it.pressed })

                            showPassword = false
                            timeLeft = 1
                        }
                    },
                    imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Outlined.Visibility,
                    contentDescription = ""
                )
            }

        }


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
            suggestion = "What u favorite color?",
            note = "I remember bla bla bla bla bla bla bla bla bla bla bla bla bla bla",
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