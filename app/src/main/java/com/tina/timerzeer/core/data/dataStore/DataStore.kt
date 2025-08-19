package com.tina.timerzeer.core.data.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

const val DATA_STORE_NAME = "settings"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)



enum class DataStoreFields{
    ENDING_ANIMATION, BACKGROUND, FONT_STYLE
}
