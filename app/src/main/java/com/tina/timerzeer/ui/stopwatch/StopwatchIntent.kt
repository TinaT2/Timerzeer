package com.tina.timerzeer.ui.stopwatch

sealed interface StopwatchIntent {
    data object Start : StopwatchIntent
    data object Pause : StopwatchIntent
    data object Resume : StopwatchIntent
    data object Stop : StopwatchIntent
    data object Tick : StopwatchIntent
}

sealed interface UserActionIntent {
    data class OnTitleChange(val name: String) : UserActionIntent
    data class OnModeChange(val mode: TimerMode) : UserActionIntent
}