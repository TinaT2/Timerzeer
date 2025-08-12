package com.tina.timerzeer.di

import com.tina.timerzeer.ui.timer.TimerViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val timerModule = module {
    viewModel { TimerViewModel() }
}