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
}

sealed interface CountDownIntent{
    data object OnSecondIncrease: CountDownIntent
    data object OnSecondDecrease: CountDownIntent
    data object OnMinutesIncrease: CountDownIntent
    data object OnMinutesDecrease: CountDownIntent
    data object OnHourIncrease: CountDownIntent
    data object OnHourDecrease: CountDownIntent
    data object OnDayIncrease: CountDownIntent
    data object OnDayDecrease: CountDownIntent
    data object ResetIsCountDownDone: CountDownIntent
    data class SetDate(val countDownInitTimer:Long): CountDownIntent
}
