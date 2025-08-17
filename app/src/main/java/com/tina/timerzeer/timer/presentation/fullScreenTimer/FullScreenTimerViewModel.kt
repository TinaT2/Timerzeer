package com.tina.timerzeer.timer.presentation.fullScreenTimer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.tina.timerzeer.app.Route
import com.tina.timerzeer.timer.presentation.Timer
import com.tina.timerzeer.timer.presentation.TimerIntent
import com.tina.timerzeer.timer.presentation.TimerMode
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


class FullScreenTimerViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    companion object {
        private const val COUNTDOWN_DONE_DELAY_MS = 2000L
    }

    val args = savedStateHandle.toRoute<Route.TimerFullScreen>()
    private val _timerState = MutableStateFlow(Timer(title = args.title, mode = args.mode))
    val timerState: StateFlow<Timer> = _timerState

    private var timerJob: Job? = null

    fun onTimerIntent(intent: TimerIntent) {
        when (intent) {
            TimerIntent.Start -> {
                if (_timerState.value.mode == TimerMode.COUNTDOWN)
                    args.time?.let {
                        _timerState.update { timer ->
                            timer.copy(elapsedTime = args.time)
                        }
                    }
                if (_timerState.value.isRunning) return
                _timerState.update { it.copy(isRunning = true) }
                startTimer()
            }

            TimerIntent.Pause -> {
                timerJob?.cancel()
                _timerState.update { it.copy(isRunning = false) }
            }

            TimerIntent.Resume -> {
                if (!_timerState.value.isRunning) {
                    _timerState.update { it.copy(isRunning = true) }
                    startTimer()
                }
            }

            TimerIntent.Stop -> {
                timerJob?.cancel()
                _timerState.update { it.copy(elapsedTime = 0L, isRunning = false) }
            }

            TimerIntent.Tick -> {
                if (!_timerState.value.isRunning) return
                if (_timerState.value.mode == TimerMode.STOPWATCH)
                    _timerState.update { it.copy(elapsedTime = it.elapsedTime + 1000) }
                else {
                    _timerState.update { it.copy(elapsedTime = it.elapsedTime - 1000) }
                    if (_timerState.value.elapsedTime == 0L) {
                        finishCountdown()
                    }
                }
            }
        }
    }

    private fun finishCountdown() {
        timerJob?.cancel()
        viewModelScope.launch {
            delay(COUNTDOWN_DONE_DELAY_MS)
            _timerState.update { it.copy(isCountDownDone = true) }
            delay(1000)
            _timerState.update {
                it.copy(
                    isRunning = false
                )
            }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000)
                onTimerIntent(TimerIntent.Tick)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

}
