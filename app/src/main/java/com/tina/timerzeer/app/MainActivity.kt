package com.tina.timerzeer.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.intPreferencesKey
import com.tina.timerzeer.R
import com.tina.timerzeer.core.data.dataStore.DataStoreFields
import com.tina.timerzeer.core.data.repository.SettingsRepository
import com.tina.timerzeer.core.presentation.components.SmoothStartUpAnimation
import com.tina.timerzeer.core.presentation.theme.TimerzeerTheme
import com.tina.timerzeer.core.presentation.theme.backgroundToIsDark
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsRepository: SettingsRepository by inject()
            var isThemeDark by remember { mutableStateOf<Boolean?>(null) }
            var typography by remember { mutableStateOf<Int?>(null) }
            var currentThemeId by remember { mutableStateOf<Int?>(null) }
            var endingAnimation by remember { mutableStateOf<Int?>(null) }
            var isLoaded by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                settingsRepository.settingsFlow.collect {
                    isThemeDark =
                        backgroundToIsDark[it[intPreferencesKey(DataStoreFields.BACKGROUND.name)]]
                    typography = it[intPreferencesKey(DataStoreFields.FONT_STYLE.name)]
                    currentThemeId = it[intPreferencesKey(DataStoreFields.BACKGROUND.name)]
                    endingAnimation = it[intPreferencesKey(DataStoreFields.ENDING_ANIMATION.name)]
                    isLoaded = true
                }
            }

            SmoothStartUpAnimation(isLoaded) {
                TimerzeerTheme(
                    darkTheme = isThemeDark ?: isSystemInDarkTheme(),
                    fontId = typography,
                    backgroundId = currentThemeId,
                    endingAnimationId = endingAnimation
                ) {
                    RequestNotificationPermission()
                    AppNavHost()
                }
            }
        }
    }
}


@Composable
fun RequestNotificationPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val context = LocalContext.current
        val launcher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
                if (!isGranted)
                    Toast.makeText(
                        context,
                        context.getString(R.string.notification_permission_denied),
                        Toast.LENGTH_LONG
                    ).show()
            }

        LaunchedEffect(Unit) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
