package com.tina.timerzeer.ui.stopwatch

sealed interface StopwatchIntent {
    data class OnTitleChange(val name: String) : StopwatchIntent
    data object Start : StopwatchIntent
    data object Pause : StopwatchIntent
    data object Resume : StopwatchIntent
    data object Stop : StopwatchIntent
    data object Tick : StopwatchIntent
}