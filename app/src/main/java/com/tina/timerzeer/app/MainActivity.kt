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
import androidx.compose.ui.text.font.FontFamily
import androidx.datastore.preferences.core.intPreferencesKey
import com.tina.timerzeer.core.data.dataStore.DataStoreFields
import com.tina.timerzeer.core.data.repository.SettingsRepository
import com.tina.timerzeer.core.presentation.theme.TimerzeerTheme
import com.tina.timerzeer.core.presentation.theme.backgroundToIsDark
import com.tina.timerzeer.core.presentation.theme.backgrounds
import com.tina.timerzeer.core.presentation.theme.fontManrope
import com.tina.timerzeer.core.presentation.theme.fontStyles
import org.koin.android.ext.android.inject
import kotlin.collections.get

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsRepository: SettingsRepository by inject()
            var isThemeDark by remember { mutableStateOf<Boolean?>(null) }
            var typography by remember { mutableStateOf<FontFamily?>(null) }
            var currentThemeId by remember { mutableStateOf<Int?>(null) }

            LaunchedEffect(Unit) {
                settingsRepository.settingsFlow.collect {
                    isThemeDark =
                        backgroundToIsDark[it[intPreferencesKey(DataStoreFields.BACKGROUND.name)]]
                    typography = fontStyles[it[intPreferencesKey(DataStoreFields.FONT_STYLE.name)]]
                    currentThemeId = it[intPreferencesKey(DataStoreFields.BACKGROUND.name)]
                }
            }
            TimerzeerTheme(darkTheme = isThemeDark ?: isSystemInDarkTheme(), fontFamily = typography?: fontManrope, isCustomisedBackground = backgrounds()[currentThemeId] != null) {
                AppNavHost()
            }
        }
    }
}