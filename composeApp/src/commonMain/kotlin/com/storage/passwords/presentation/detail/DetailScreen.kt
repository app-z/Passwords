package com.storage.passwords.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.storage.passwords.models.PasswordItem
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import passwords.composeapp.generated.resources.Detail
import passwords.composeapp.generated.resources.Res


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    paddingValues: PaddingValues,
    password_id: String,
    navController: NavHostController
) {

    val scope = rememberCoroutineScope()

    val viewModel =
        koinViewModel<DetailViewModel>(
            parameters = {
                parametersOf(password_id)
            }
        )

    val state by viewModel.state.collectAsStateWithLifecycle()

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

        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
//                .padding(paddingValues)
                .fillMaxSize()
        ) {
            DetailScreenImpl(state.passwordItem)
        }
}

@Composable
fun DetailScreenImpl(
    passwordItem: PasswordItem) {
    Text(text = passwordItem.name)
    Text(text = passwordItem.password)
    Text(text = passwordItem.note)
}
