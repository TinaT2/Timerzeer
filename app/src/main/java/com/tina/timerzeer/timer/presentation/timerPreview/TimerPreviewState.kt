package com.tina.timerzeer.timer.presentation.timerPreview

import com.tina.timerzeer.R
import com.tina.timerzeer.core.domain.TimerMode
import com.tina.timerzeer.core.domain.TimerZeerError


data class TimerPreviewState(
    val stopwatchTitle: String = "",
    val countdownTitle: String = "",
    val countDownInitTime: Long = 0L,
    val mode: TimerMode = TimerMode.STOPWATCH,
    val errorMessage: TimerZeerError? = null
) {
    fun getTitle() = if (mode == TimerMode.STOPWATCH) stopwatchTitle else countdownTitle

    fun getPlaceHolder() =
        if (mode == TimerMode.STOPWATCH) R.string.stopwatch_title else R.string.countdown_title

    fun getOnTitleChange(title: String) =
        if (mode == TimerMode.STOPWATCH) TimerPreviewIntent.OnStopwatchTitleChange(title) else TimerPreviewIntent.OnCountDownTitleChange(
            title
        )
}