package com.storage.passwords.presentation.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.storage.passwords.models.PasswordItem
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf


@Composable
fun DetailScreen(
    password_id: String
)
{

    val viewModel =
        koinViewModel<DetailViewModel>(
            parameters = {
                parametersOf(password_id)
            }
        )

    val state by viewModel.state.collectAsStateWithLifecycle()

    DetailScreenImpl(state.passwordItem)

}

@Composable
fun DetailScreenImpl(passwordItem: PasswordItem) {
    Column {
        Text(passwordItem.name)
    }
}
