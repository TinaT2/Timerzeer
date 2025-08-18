package com.tina.timerzeer.timer.presentation

sealed interface TimerIntent {
    data object Start : TimerIntent
    data object Pause : TimerIntent
    data object Resume : TimerIntent
    data object Stop : TimerIntent
    data object Tick : TimerIntent
}

sealed interface UserActionIntent {
    data class OnStopwatchTitleChange(val name: String) : UserActionIntent
    data class OnCountDownTitleChange(val name: String) : UserActionIntent
    data class OnModeChange(val mode: TimerMode) : UserActionIntent
    data object OnSecondIncrease: UserActionIntent
    data object OnSecondDecrease: UserActionIntent
    data object OnMinutesIncrease: UserActionIntent
    data object OnMinutesDecrease: UserActionIntent
    data object OnHourIncrease: UserActionIntent
    data object OnHourDecrease: UserActionIntent
    data object OnDayIncrease: UserActionIntent
    data object OnDayDecrease: UserActionIntent
    data class SetDate(val countDownInitTimer:Long): UserActionIntent
}

sealed interface UiOverlay {
    data object None : UiOverlay
    data object TimerStyle : UiOverlay
    data object BackgroundTheme : UiOverlay
    data object EndingAnimation : UiOverlay
    data object DatePicker : UiOverlay
}
