package com.tina.timerzeer.di

import com.tina.timerzeer.timer.presentation.TimerViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val timerModule = module {
    viewModel { TimerViewModel() }
}