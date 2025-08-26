package com.tina.timerzeer.timer.presentation.fullScreenTimer

import com.tina.timerzeer.core.domain.TimerMode
import com.tina.timerzeer.core.domain.TimerZeerError


data class Timer(
    val title: String,
    val mode: TimerMode,
    val elapsedTime: Long = 0L,
    val isRunning: Boolean = false,
    val errorMessage: TimerZeerError? = null,
    val isCountDownDone: Boolean = false,
    val hide: Boolean = false,
    val lock: Boolean = false,
    val iconAppear: Boolean = true
)