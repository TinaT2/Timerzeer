package com.tina.timerzeer.di

import androidx.lifecycle.SavedStateHandle
import com.tina.timerzeer.ui.stopwatch.StopwatchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val timerModule = module {
    viewModel { (handle: SavedStateHandle) -> StopwatchViewModel(handle) }
}