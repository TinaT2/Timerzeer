package com.tina.timerzeer.ui.stopwatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class StopwatchViewModel() : ViewModel() {
    private val _stopwatchState = MutableStateFlow(StopWatchState())
    val stopwatchState: StateFlow<StopWatchState> = _stopwatchState
    private val _userActionState = MutableStateFlow(UserActionState())
    val userActionState: StateFlow<UserActionState> = _userActionState

    private var timerJob: Job? = null

    fun onStopwatchIntent(intent: StopwatchIntent) {
        when (intent) {
            StopwatchIntent.Start -> {
                if (_stopwatchState.value.isRunning) return
                _stopwatchState.update { it.copy(isRunning = true) }
                startTimer()
            }

            StopwatchIntent.Pause -> {
                timerJob?.cancel()
                _stopwatchState.update { it.copy(isRunning = false) }
            }

            StopwatchIntent.Resume -> {
                if (!_stopwatchState.value.isRunning) {
                    _stopwatchState.update { it.copy(isRunning = true) }
                    startTimer()
                }
            }

            StopwatchIntent.Stop -> {
                timerJob?.cancel()
                _stopwatchState.update { it.copy(elapsedTime = 0L, isRunning = false) }
            }

            StopwatchIntent.Tick -> {
                _stopwatchState.update { it.copy(elapsedTime = it.elapsedTime + 1000) }
            }
        }
    }

    fun onUserAction(action: UserActionIntent) {
        when (action) {
            is UserActionIntent.OnTitleChange -> {
                _userActionState.update {
                    it.copy(title = action.name)
                }
            }

            is UserActionIntent.OnModeChange -> {
                _userActionState.update {
                    if (it.mode != action.mode) {
                        it.copy(mode = action.mode)
                    } else it
                }
            }
        }
    }


    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000)
                onStopwatchIntent(StopwatchIntent.Tick)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

}
