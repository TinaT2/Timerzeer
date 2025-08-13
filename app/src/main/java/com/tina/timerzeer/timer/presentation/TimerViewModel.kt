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

    companion object {
        private const val COUNTDOWN_DONE_DELAY_MS = 2000L
    }

    private val _timerState = MutableStateFlow(Timer())
    val timerState: StateFlow<Timer> = _timerState
    private val _userActionState = MutableStateFlow(UserActionState())
    val userActionState: StateFlow<UserActionState> = _userActionState

    private var timerJob: Job? = null

    fun onTimerIntent(intent: TimerIntent) {
        when (intent) {
            TimerIntent.Start -> {
                if (userActionState.value.mode == TimerMode.COUNTDOWN)
                    _timerState.update {
                        it.copy(elapsedTime = it.countDownInitTime)
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
                if (userActionState.value.mode == TimerMode.STOPWATCH)
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
            CountDownIntent.OnHourDecrease -> {
                _timerState.update { it.copy(countDownInitTime = it.countDownInitTime.minusHour()) }
            }

            CountDownIntent.OnHourIncrease -> {
                _timerState.update { it.copy(countDownInitTime = it.countDownInitTime.plusHour()) }
            }

            CountDownIntent.OnMinutesDecrease -> {
                _timerState.update { it.copy(countDownInitTime = it.countDownInitTime.minusMinute()) }
            }

            CountDownIntent.OnMinutesIncrease -> {
                _timerState.update { it.copy(countDownInitTime = it.countDownInitTime.plusMinute()) }
            }

            CountDownIntent.OnSecondDecrease -> {
                _timerState.update { it.copy(countDownInitTime = it.countDownInitTime.minusSecond()) }
            }

            CountDownIntent.OnSecondIncrease -> {
                _timerState.update { it.copy(countDownInitTime = it.countDownInitTime.plusSecond()) }
            }

            CountDownIntent.ResetIsCountDownDone -> {
                _timerState.update { it.copy(isCountDownDone = false) }
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
