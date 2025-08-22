package com.storage.passwords.di

import com.storage.passwords.presentation.detail.DetailViewModel
import com.storage.passwords.presentation.passwords.PasswordsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewmodelModule = module {
    viewModelOf(::PasswordsViewModel)
    viewModelOf(::DetailViewModel)
}