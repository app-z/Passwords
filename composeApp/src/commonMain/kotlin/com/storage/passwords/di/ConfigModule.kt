package com.storage.passwords.di

import com.storage.passwords.repository.ConfigRepository
import org.koin.dsl.module

val configModule = module {
    single { ConfigRepository() }
}
