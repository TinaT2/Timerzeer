package com.tina.timerzeer.timer.presentation

import com.tina.timerzeer.R
import com.tina.timerzeer.core.domain.TimerZeerError

data class Timer(
    val elapsedTime: Long = 0L,
    val isRunning: Boolean = false,
    val errorMessage: TimerZeerError? = null,
    val countDownInitTime: Long = 0L,
    val isCountDownDone: Boolean = false,
)

data class UserActionState(
    val timerTitle: String = "",
    val countdownTitle: String = "",
    val mode: TimerMode = TimerMode.STOPWATCH,
    val onThemeChangeClicked: () -> Unit = {},
    val onBackgroundChangeClicked: () -> Unit = {},
    val onAnimationChangeClicked: () -> Unit = {},
)

enum class TimerMode(val nameId: Int) {
    STOPWATCH(R.string.stopwatch), COUNTDOWN(R.string.countdown)
}