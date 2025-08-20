package com.storage.passwords.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            configModule,
            databaseModule,
            dataStoreModule,
            networkModule,
            repositoryModule,
            useCaseModule,
            viewmodelModule
        )
    }