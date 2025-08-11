package com.tina.timerzeer.ui.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tina.timerzeer.mapper.minusHour
import com.tina.timerzeer.mapper.minusMinute
import com.tina.timerzeer.mapper.minusSecond
import com.tina.timerzeer.mapper.plusHour
import com.tina.timerzeer.mapper.plusMinute
import com.tina.timerzeer.mapper.plusSecond
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TimerViewModel() : ViewModel() {
    private val _stopwatchState = MutableStateFlow(Timer())
    val stopwatchState: StateFlow<Timer> = _stopwatchState
    private val _userActionState = MutableStateFlow(UserActionState())
    val userActionState: StateFlow<UserActionState> = _userActionState

    private var timerJob: Job? = null

    fun onTimerIntent(intent: TimerIntent) {
        when (intent) {
            TimerIntent.Start -> {
                if (_stopwatchState.value.isRunning) return
                _stopwatchState.update { it.copy(isRunning = true) }
                startTimer()
            }

            TimerIntent.Pause -> {
                timerJob?.cancel()
                _stopwatchState.update { it.copy(isRunning = false) }
            }

            TimerIntent.Resume -> {
                if (!_stopwatchState.value.isRunning) {
                    _stopwatchState.update { it.copy(isRunning = true) }
                    startTimer()
                }
            }

            TimerIntent.Stop -> {
                timerJob?.cancel()
                _stopwatchState.update { it.copy(elapsedTime = 0L, isRunning = false) }
            }

            TimerIntent.Tick -> {
                if (!_stopwatchState.value.isRunning) return
                if (userActionState.value.mode == TimerMode.STOPWATCH)
                    _stopwatchState.update { it.copy(elapsedTime = it.elapsedTime + 1000) }
                else {
                    _stopwatchState.update { it.copy(elapsedTime = it.elapsedTime - 1000) }
                    if (_stopwatchState.value.elapsedTime == 0L)
                        onTimerIntent(TimerIntent.Stop)
                }
            }
        }
    }

    fun onUserAction(action: UserActionIntent) {
        when (action) {
            is UserActionIntent.OnStopwatchTitleChange -> {
                _userActionState.update {
                    it.copy(stopwatchTitle = action.name)
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
                _stopwatchState.update { it.copy(elapsedTime = it.elapsedTime.minusHour()) }
            }

            CountDownIntent.onHourIncrease -> {
                _stopwatchState.update { it.copy(elapsedTime = it.elapsedTime.plusHour()) }
            }

            CountDownIntent.onMinutesDecrease -> {
                _stopwatchState.update { it.copy(elapsedTime = it.elapsedTime.minusMinute()) }
            }

            CountDownIntent.onMinutesIncrease -> {
                _stopwatchState.update { it.copy(elapsedTime = it.elapsedTime.plusMinute()) }
            }

            CountDownIntent.onSecondDecrease -> {
                _stopwatchState.update { it.copy(elapsedTime = it.elapsedTime.minusSecond()) }
            }

            CountDownIntent.onSecondIncrease -> {
                _stopwatchState.update { it.copy(elapsedTime = it.elapsedTime.plusSecond()) }
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
