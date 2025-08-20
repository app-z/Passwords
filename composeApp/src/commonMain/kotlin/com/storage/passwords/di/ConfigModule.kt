package com.storage.passwords.di

import com.storage.passwords.repository.ConfigRepository
import com.storage.passwords.utils.AppPreferences
import org.koin.dsl.module

val configModule = module {
    single { ConfigRepository() }
    single { AppPreferences(get()) }
}
