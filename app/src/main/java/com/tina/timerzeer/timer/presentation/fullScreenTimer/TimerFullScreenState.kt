package com.tina.timerzeer.timer.presentation.fullScreenTimer

import com.tina.timerzeer.core.domain.TimerMode
import com.tina.timerzeer.core.domain.TimerZeerError

//todo change name
data class Timer(
    val title: String = "",
    val mode: TimerMode = TimerMode.STOPWATCH,
    val initialTime: Long? = null,
    val elapsedTime: Long = 0L,
    val isRunning: Boolean = false,
    val errorMessage: TimerZeerError? = null,
    val isCountDownDone: Boolean = false,
)

data class TimerUiState(
    val hide: Boolean = false,
    val lock: Boolean = false,
    val iconAppear: Boolean = false
)

data class FullScreenTimerState(
    val timer: Timer,
    val ui: TimerUiState
)