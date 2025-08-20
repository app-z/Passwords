package com.spacex.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.storage.passwords.database.AppDatabase
import com.storage.passwords.database.getDatabaseBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module

val databaseModule = module {

    // database
    single {
        getRoomDatabase(getDatabaseBuilder())
    }

}


fun getRoomDatabase(
    builder: RoomDatabase.Builder<AppDatabase>
): AppDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .fallbackToDestructiveMigration(
            dropAllTables = true
        )
        .build()
}

