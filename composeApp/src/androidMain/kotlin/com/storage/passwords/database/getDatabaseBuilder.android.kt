package com.storage.passwords.database

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import org.koin.mp.KoinPlatform

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val appContext = KoinPlatform.getKoin().get<Application>()
    val dbFile = appContext.getDatabasePath(DB_Name)

    System.loadLibrary("sqlcipher");

    val passphrase: ByteArray = SQLiteDatabase.getBytes("password".toCharArray())
    val factory = SupportFactory(passphrase)

    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    ).openHelperFactory(factory)

}