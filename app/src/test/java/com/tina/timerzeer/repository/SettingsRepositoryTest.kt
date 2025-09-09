package com.tina.timerzeer.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import com.tina.timerzeer.core.data.repository.SettingsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsRepositoryTest {

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var repository: SettingsRepository

    @BeforeEach
    fun setup() {
        val testFile = File.createTempFile("datastore-test", ".preferences_pb")
        dataStore = PreferenceDataStoreFactory.create { testFile }
        repository = SettingsRepository(dataStore)
    }


    @Test
    fun `saveEndingAnimation actually saves the preference`() = runTest {
        val key = intPreferencesKey("ENDING_ANIMATION")
        repository.saveEndingAnimation(42)

        val prefs = dataStore.data.first()
        assertEquals(42, prefs[key])
    }

    @Test
    fun `saveBackgroundTheme actually saves the preference`() = runTest {
        val key = intPreferencesKey("BACKGROUND")
        repository.saveBackgroundTheme(7)

        val prefs = dataStore.data.first()
        assertEquals(7, prefs[key])
    }

    @Test
    fun `saveFontStyle actually saves the preference`() = runTest {
        val key = intPreferencesKey("FONT_STYLE")
        repository.saveFontStyle(99)

        val prefs = dataStore.data.first()
        assertEquals(99, prefs[key])
    }
}