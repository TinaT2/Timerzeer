package com.tina.timerzeer.timer.presentation.timerPreview

import com.tina.timerzeer.core.domain.TimerMode
import com.tina.timerzeer.core.domain.TimerZeerError


data class TimerPreviewState(
    val timerTitle: String = "",
    val countdownTitle: String = "",
    val countDownInitTime: Long = 0L,
    val mode: TimerMode = TimerMode.STOPWATCH,
    val errorMessage: TimerZeerError? = null,
)