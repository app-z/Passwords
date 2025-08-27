package com.storage.passwords.utils

import org.jetbrains.compose.resources.StringResource
import passwords.composeapp.generated.resources.Res
import passwords.composeapp.generated.resources.dark_mode
import passwords.composeapp.generated.resources.light_mode
import passwords.composeapp.generated.resources.system_default

object Const {
    val PASSWORD_ID_PARAM = "PASSWORD_ID_PARAM"

    enum class Theme(val title: StringResource) {
        SYSTEM_DEFAULT(Res.string.system_default),
        LIGHT_MODE(Res.string.light_mode),
        DARK_MODE(Res.string.dark_mode)
    }
}