package com.tina.timerzeer.timer.presentation.timerPreview

import com.tina.timerzeer.core.domain.TimerMode


sealed interface TimerPreviewIntent {
    data class OnStopwatchTitleChange(val name: String) : TimerPreviewIntent
    data class OnCountDownTitleChange(val name: String) : TimerPreviewIntent
    data class OnModeChange(val mode: TimerMode) : TimerPreviewIntent
    data object OnSecondIncrease : TimerPreviewIntent
    data object OnSecondDecrease : TimerPreviewIntent
    data object OnMinutesIncrease : TimerPreviewIntent
    data object OnMinutesDecrease : TimerPreviewIntent
    data object OnHourIncrease : TimerPreviewIntent
    data object OnHourDecrease : TimerPreviewIntent
    data object OnDayIncrease : TimerPreviewIntent
    data object OnDayDecrease : TimerPreviewIntent
    data class SetDate(val countDownInitTimer: Long) : TimerPreviewIntent
    data class SetEndingAnimation(val endingAnimation: Int): TimerPreviewIntent
    data class SetBackground(val backgroundId: Int): TimerPreviewIntent
}

sealed interface UiOverlayIntent {
    data object None : UiOverlayIntent
    data object TimerStyle : UiOverlayIntent
    data object BackgroundTheme : UiOverlayIntent
    data object EndingAnimation : UiOverlayIntent
    data object DatePicker : UiOverlayIntent
}
