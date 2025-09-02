package com.tina.timerzeer.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.tina.timerzeer.core.data.dataStore.dataStore
import com.tina.timerzeer.core.data.repository.SettingsRepository
import com.tina.timerzeer.timer.data.repository.TimerRepository
import com.tina.timerzeer.timer.presentation.fullScreenTimer.FullScreenTimerViewModel
import com.tina.timerzeer.timer.presentation.timerPreview.TimerPreviewViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val timerModule = module {
    viewModelOf(::TimerPreviewViewModel)
    viewModelOf(::FullScreenTimerViewModel)
    single<DataStore<Preferences>> {
        androidContext().dataStore
    }
    singleOf(::SettingsRepository)
    singleOf(::TimerRepository)
}