package com.tina.timerzeer.ui.timer

import com.tina.timerzeer.R
import com.tina.timerzeer.core.domain.TimerZeerError

data class Timer(
    val elapsedTime: Long = 0L,
    val isRunning: Boolean = false,
    val errorMessage: TimerZeerError? = null
)

data class UserActionState(
    val stopwatchTitle: String = "",
    val countdownTitle: String = "",
    val mode: TimerMode = TimerMode.STOPWATCH
)

enum class TimerMode(val nameId: Int) {
    STOPWATCH(R.string.stopwatch), COUNTDOWN(R.string.countdown)
}