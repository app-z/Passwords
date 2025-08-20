package com.storage.passwords

import androidx.compose.ui.window.ComposeUIViewController
import com.storage.passwords.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}
