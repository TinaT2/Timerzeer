package com.tina.timerzeer.timer.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TimerRepository {
    private val _timeFlow = MutableStateFlow(0L)
    val timeFlow: StateFlow<Long> = _timeFlow.asStateFlow()

    fun update(seconds: Long) {
        _timeFlow.value = seconds
    }

    fun reset() {
        _timeFlow.value = 0
    }
}