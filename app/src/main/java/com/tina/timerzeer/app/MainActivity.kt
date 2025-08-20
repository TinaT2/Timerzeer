package com.tina.timerzeer.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.intPreferencesKey
import com.tina.timerzeer.core.data.dataStore.DataStoreFields
import com.tina.timerzeer.core.data.repository.SettingsRepository
import com.tina.timerzeer.core.theme.TimerzeerTheme
import com.tina.timerzeer.core.theme.backgroundToIsDark
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsRepository: SettingsRepository by inject()
            var isThemeDark by remember { mutableStateOf<Boolean?>(null) }
            LaunchedEffect(Unit) {
                settingsRepository.settingsFlow.collect {
                    isThemeDark =
                        backgroundToIsDark[it[intPreferencesKey(DataStoreFields.BACKGROUND.name)]]
                }
            }
            TimerzeerTheme(darkTheme = isThemeDark ?: isSystemInDarkTheme()) {
                AppNavHost()
            }
        }
    }
}