package com.storage.passwords.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences


const val dataStoreFileName = "setting.preferences_pb"
expect fun createDataStore(): DataStore<Preferences>
