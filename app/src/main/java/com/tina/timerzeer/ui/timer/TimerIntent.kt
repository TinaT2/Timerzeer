package com.tina.timerzeer.ui.timer

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
    data object onSecondIncrease: CountDownIntent
    data object onSecondDecrease: CountDownIntent
    data object onMinutesIncrease: CountDownIntent
    data object onMinutesDecrease: CountDownIntent
    data object onHourIncrease: CountDownIntent
    data object onHourDecrease: CountDownIntent
}
