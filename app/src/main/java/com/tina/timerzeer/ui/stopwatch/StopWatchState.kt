package com.tina.timerzeer.ui.stopwatch

import com.tina.timerzeer.core.domain.TimerZeerError

data class StopWatchState(
    val title: String = "",
    val elapsedTime: Long = 0L,
    val isRunning: Boolean = false,
    val errorMessage: TimerZeerError? = null
)