package com.storage.passwords.di

import com.storage.passwords.database.AppDatabase
import com.storage.passwords.repository.LocalRepository
import com.storage.passwords.repository.NetworkRepository
import org.koin.dsl.module

val repositoryModule = module {
    single {
        LocalRepository(get<AppDatabase>().passwordsDao())
    }

    single {
        NetworkRepository(get())
    }
}