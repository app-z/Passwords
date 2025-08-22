package com.storage.passwords

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.storage.passwords.presentation.detail.DetailScreen
import com.storage.passwords.presentation.menu.BurgerMenu
import com.storage.passwords.presentation.passwords.PasswordsScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {

    val onAddItem: () -> Unit = {}
    val onEditItem: () -> Unit = {}

    var openDetailScreen by remember { mutableStateOf("-1") }

    val scope = rememberCoroutineScope()

    MaterialTheme {

        BurgerMenu(
            onAddItem = {
                onAddItem.invoke()
                onEditItem.invoke()
            },
            onEditItem = {}
        ) { paddingValue ->
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .safeContentPadding()
                    .padding(paddingValue)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                PasswordsScreen(
                    currentItem = { password_id ->
                        openDetailScreen = password_id
                        scope.launch {
                            delay(5000)
                            openDetailScreen = "-1"
                        }
                    }
                )
            }

            if (openDetailScreen != "-1") {
                OpenDetailScreen(openDetailScreen, paddingValues = paddingValue)
            }

        }
    }


}

@Composable
fun OpenDetailScreen(password_id: String, paddingValues: PaddingValues) {
    DetailScreen(
        password_id = password_id,
        paddingValues = paddingValues
    )
}
