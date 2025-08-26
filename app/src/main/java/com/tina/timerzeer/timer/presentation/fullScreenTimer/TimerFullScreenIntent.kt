package com.tina.timerzeer.timer.presentation.fullScreenTimer

sealed interface TimerFullScreenIntent {
    data object Start : TimerFullScreenIntent
    data object Pause : TimerFullScreenIntent
    data object Resume : TimerFullScreenIntent
    data object Stop : TimerFullScreenIntent
    data object Tick : TimerFullScreenIntent
    data object Hide : TimerFullScreenIntent
    data object Lock : TimerFullScreenIntent
    data object IconAppear : TimerFullScreenIntent
}