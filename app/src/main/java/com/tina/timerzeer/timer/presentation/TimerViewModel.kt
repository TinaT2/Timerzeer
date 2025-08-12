package com.tina.timerzeer.timer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tina.timerzeer.timer.data.mapper.minusHour
import com.tina.timerzeer.timer.data.mapper.minusMinute
import com.tina.timerzeer.timer.data.mapper.minusSecond
import com.tina.timerzeer.timer.data.mapper.plusHour
import com.tina.timerzeer.timer.data.mapper.plusMinute
import com.tina.timerzeer.timer.data.mapper.plusSecond
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TimerViewModel() : ViewModel() {
    private val _timerState = MutableStateFlow(Timer())
    val timerState: StateFlow<Timer> = _timerState
    private val _userActionState = MutableStateFlow(UserActionState())
    val userActionState: StateFlow<UserActionState> = _userActionState

    private var timerJob: Job? = null

    fun onTimerIntent(intent: TimerIntent) {
        when (intent) {
            TimerIntent.Start -> {
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
                if (userActionState.value.mode == TimerMode.STOPWATCH)
                    _timerState.update { it.copy(elapsedTime = it.elapsedTime + 1000) }
                else {
                    _timerState.update { it.copy(elapsedTime = it.elapsedTime - 1000) }
                    if (_timerState.value.elapsedTime == 0L)
                        onTimerIntent(TimerIntent.Stop)
                }
            }
        }
    }

    fun onUserAction(action: UserActionIntent) {
        when (action) {
            is UserActionIntent.OnStopwatchTitleChange -> {
                _userActionState.update {
                    it.copy(timerTitle = action.name)
                }
            }

            is UserActionIntent.OnModeChange -> {
                _userActionState.update {
                    if (it.mode != action.mode) {
                        it.copy(mode = action.mode)
                    } else it
                }
            }

            is UserActionIntent.OnCountDownTitleChange -> {
                _userActionState.update {
                    it.copy(countdownTitle = action.name)
                }
            }
        }
    }

    fun onCountDownIntent(intent: CountDownIntent) {
        when (intent) {
            CountDownIntent.onHourDecrease -> {
                _timerState.update { it.copy(elapsedTime = it.elapsedTime.minusHour()) }
            }

            CountDownIntent.onHourIncrease -> {
                _timerState.update { it.copy(elapsedTime = it.elapsedTime.plusHour()) }
            }

            CountDownIntent.onMinutesDecrease -> {
                _timerState.update { it.copy(elapsedTime = it.elapsedTime.minusMinute()) }
            }

            CountDownIntent.onMinutesIncrease -> {
                _timerState.update { it.copy(elapsedTime = it.elapsedTime.plusMinute()) }
            }

            CountDownIntent.onSecondDecrease -> {
                _timerState.update { it.copy(elapsedTime = it.elapsedTime.minusSecond()) }
            }

            CountDownIntent.onSecondIncrease -> {
                _timerState.update { it.copy(elapsedTime = it.elapsedTime.plusSecond()) }
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
