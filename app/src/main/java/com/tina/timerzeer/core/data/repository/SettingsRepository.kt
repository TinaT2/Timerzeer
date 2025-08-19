package com.tina.timerzeer.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.tina.timerzeer.core.data.dataStore.DataStoreFields

class SettingsRepository(private val dataStore: DataStore<Preferences>) {

    val settingsFlow = dataStore.data

    suspend fun saveEndingAnimation(animationId: Int) {
        dataStore.edit { preferences ->
            preferences[intPreferencesKey(DataStoreFields.ENDING_ANIMATION.name)] =
                animationId
        }
    }

    suspend fun saveBackgroundTheme(backgroundId: Int) {
        dataStore.edit { preferences ->
            preferences[intPreferencesKey(DataStoreFields.BACKGROUND.name)] =
                backgroundId
        }
    }

    suspend fun saveFontStyle(fontStyle: Int) {
        dataStore.edit { preferences ->
            preferences[intPreferencesKey(DataStoreFields.FONT_STYLE.name)] = fontStyle
        }
    }
}