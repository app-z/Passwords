package com.storage.passwords

import androidx.compose.ui.window.ComposeUIViewController
import com.spacex.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}
