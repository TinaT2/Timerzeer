package com.tina.timerzeer.di

import com.tina.timerzeer.ui.stopwatch.StopwatchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val timerModule = module {
    viewModel { StopwatchViewModel() }
}