package com.storage.passwords.presentation.settings

import androidx.lifecycle.viewModelScope
import com.spacex.utils.UiText
import com.storage.passwords.utils.AppPreferences
import com.storage.passwords.utils.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class SettingsViewModel(
    private val appPreferences: AppPreferences,
) : BaseViewModel<SettingsEvent, SettingsState, SettingsEffect>(SettingsState()) {


    override fun onCoroutineException(message: UiText) {
        setEffect {
            SettingsEffect.CoroutineError(message)
        }
    }

    override fun runInitialEvent() {
        currentThemeGet()
    }

    override fun handleEvents(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.Theme -> changeThemeMode(event.currentThene)
            SettingsEvent.NavigationBack -> navigationBack()
        }
    }

    private fun navigationBack() {
        setEffect {
            SettingsEffect.NavigationBack
        }
    }

    private val mutex = Mutex()

    private fun currentThemeGet() {
        defaultViewModelScope.launch {
            val currentTheme =
                mutex.withLock {
                    appPreferences.getTheme()
                }
            setState {
                copy(currentTheme = currentTheme)
            }
        }
    }

    private fun changeThemeMode(mode: String) = viewModelScope.launch(Dispatchers.IO) {
        appPreferences.changeThemeMode(mode)
        setState {
            copy(currentTheme = mode)
        }
    }

}