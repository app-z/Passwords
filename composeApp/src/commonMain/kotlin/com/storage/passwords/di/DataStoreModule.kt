package com.storage.passwords.di

import com.storage.passwords.datastore.createDataStore
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataStoreModule = module {

    singleOf( ::createDataStore)

}
