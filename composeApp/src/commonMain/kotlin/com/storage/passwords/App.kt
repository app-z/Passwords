package com.storage.passwords

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.storage.passwords.presentation.menu.navigation.NavigationApplication
import com.storage.passwords.presentation.settings.SettingsViewModel
import com.storage.passwords.theme.PasswordsTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {

    val settingViewModel = koinViewModel<SettingsViewModel>()
    val currentTheme by settingViewModel.viewState.collectAsStateWithLifecycle()
    PasswordsTheme(currentTheme.currentTheme) {
        NavigationApplication(settingViewModel)
    }

}



