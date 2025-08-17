package com.tina.timerzeer.di

import androidx.compose.ui.text.font.Font
import com.tina.timerzeer.timer.presentation.TimerViewModel
import com.tina.timerzeer.timer.presentation.fullScreenTimer.FullScreenTimerViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val timerModule = module {
    viewModel { TimerViewModel() }
    viewModelOf(::FullScreenTimerViewModel)
}