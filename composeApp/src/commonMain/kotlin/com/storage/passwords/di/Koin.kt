package com.spacex.di

import com.storage.passwords.di.networkModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            databaseModule,
            networkModule,
            repositoryModule,
            viewmodelModule
        )
    }