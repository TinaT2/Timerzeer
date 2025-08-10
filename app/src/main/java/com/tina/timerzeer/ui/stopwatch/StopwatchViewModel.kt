package com.tina.timerzeer.ui.stopwatch

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class StopwatchViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _state = MutableStateFlow(StopWatchState())
    val state: StateFlow<StopWatchState> = _state

    companion object {
        private const val KEY_SELECTED_INDEX = "selected_index"
    }

    private var timerJob: Job? = null
    private val _selectedIndex = savedStateHandle.getStateFlow(KEY_SELECTED_INDEX, 0)
    val selectedTabIndex: StateFlow<Int> = _selectedIndex

    fun onIntent(intent: StopwatchIntent) {
        when (intent) {
            is StopwatchIntent.OnTitleChange -> {
                _state.update {
                    it.copy(title = intent.name)
                }
            }

            StopwatchIntent.Start -> {
                if (_state.value.isRunning) return
                _state.update { it.copy(isRunning = true) }
                startTimer()
            }

            StopwatchIntent.Pause -> {
                timerJob?.cancel()
                _state.update { it.copy(isRunning = false) }
            }

            StopwatchIntent.Resume -> {
                if (!_state.value.isRunning) {
                    _state.update { it.copy(isRunning = true) }
                    startTimer()
                }
            }

            StopwatchIntent.Stop -> {
                timerJob?.cancel()
                _state.update { it.copy(elapsedTime = 0L, isRunning = false) }
            }

            StopwatchIntent.Tick -> {
                _state.update { it.copy(elapsedTime = it.elapsedTime + 1000) }
            }
        }
    }


    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000)
                onIntent(StopwatchIntent.Tick)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

}
