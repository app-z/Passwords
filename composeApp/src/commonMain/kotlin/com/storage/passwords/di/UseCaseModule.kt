package com.storage.passwords.di

import com.storage.passwords.usecase.LoadFromInternetUseCase
import org.koin.dsl.module

val useCaseModule = module {
        single { LoadFromInternetUseCase(get(), get()) }
    }
