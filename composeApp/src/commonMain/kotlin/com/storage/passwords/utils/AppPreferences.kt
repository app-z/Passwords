package com.storage.passwords.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


class AppPreferences(
    private val dataStore: DataStore<Preferences>
) {

    private val themeKey = stringPreferencesKey("com.spacex/theme")
    private val rowModeKey = stringPreferencesKey("com.spacex/rowmode")


    suspend fun getTheme() = dataStore.data.map { preferences ->
        preferences[themeKey] ?: Const.Theme.DARK_MODE.name
    }.first()

    suspend fun changeThemeMode(value: String) = dataStore.edit { preferences ->
        preferences[themeKey] = value
    }

    suspend fun setRowMode(rowMode: String) = dataStore.edit { preferences ->
        preferences[rowModeKey] = rowMode
    }

    suspend fun getRowMode() = dataStore.data.map { preferences ->
        preferences[rowModeKey] ?: CARD_MODE
    }.first()

    fun getRowModeFlow() = dataStore.data.map { preferences ->
        preferences[rowModeKey] ?: CARD_MODE
    }


    companion object {
        const val ROW_MODE = "ROW_MODE"
        const val CARD_MODE = "CARD_MODE"
    }

}
