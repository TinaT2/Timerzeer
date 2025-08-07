package com.tina.timerzeer.ui.stopwatch

data class StopWatchState(
    val title: String? = null,
    val elapsedTime: Long = 0L,
    val isRunning: Boolean = false
)