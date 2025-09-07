package com.tina.timerzeer.timer.presentation.fullScreenTimer

sealed interface TimerFullScreenIntent {
    data object Start : TimerFullScreenIntent
    data object Pause : TimerFullScreenIntent
    data object Resume : TimerFullScreenIntent
    data object Stop : TimerFullScreenIntent
    data object Hide : TimerFullScreenIntent
    data object Lock : TimerFullScreenIntent
    data object IconAppear : TimerFullScreenIntent
}

sealed interface TimerIntent {
    data object Start : TimerIntent
    data object Pause : TimerIntent
    data object Resume : TimerIntent
    data object Stop : TimerIntent
}

fun TimerFullScreenIntent.toTimerIntent(): TimerIntent? = when (this) {
    TimerFullScreenIntent.Start -> TimerIntent.Start
    TimerFullScreenIntent.Pause -> TimerIntent.Pause
    TimerFullScreenIntent.Resume -> TimerIntent.Resume
    TimerFullScreenIntent.Stop -> TimerIntent.Stop
    else -> null
}