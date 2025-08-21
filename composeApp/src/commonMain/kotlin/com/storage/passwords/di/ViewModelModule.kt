package com.storage.passwords.di

import com.storage.passwords.presentation.PasswordsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewmodelModule = module {
    viewModelOf(::PasswordsViewModel)
}