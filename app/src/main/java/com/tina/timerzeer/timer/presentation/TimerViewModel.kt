package com.tina.timerzeer.timer.presentation

import androidx.lifecycle.ViewModel
import com.tina.timerzeer.timer.data.mapper.minusDay
import com.tina.timerzeer.timer.data.mapper.minusHour
import com.tina.timerzeer.timer.data.mapper.minusMinute
import com.tina.timerzeer.timer.data.mapper.minusSecond
import com.tina.timerzeer.timer.data.mapper.plusDay
import com.tina.timerzeer.timer.data.mapper.plusHour
import com.tina.timerzeer.timer.data.mapper.plusMinute
import com.tina.timerzeer.timer.data.mapper.plusSecond
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update


class TimerViewModel() : ViewModel() {

    private val _userActionState = MutableStateFlow(UserActionState())
    val userActionState: StateFlow<UserActionState> = _userActionState

    private var timerJob: Job? = null

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
            UserActionIntent.OnHourDecrease -> {
                _userActionState.update { it.copy(countDownInitTime = it.countDownInitTime.minusHour()) }
            }

            UserActionIntent.OnHourIncrease -> {
                _userActionState.update { it.copy(countDownInitTime = it.countDownInitTime.plusHour()) }
            }

            UserActionIntent.OnMinutesDecrease -> {
                _userActionState.update { it.copy(countDownInitTime = it.countDownInitTime.minusMinute()) }
            }

            UserActionIntent.OnMinutesIncrease -> {
                _userActionState.update { it.copy(countDownInitTime = it.countDownInitTime.plusMinute()) }
            }

            UserActionIntent.OnSecondDecrease -> {
                _userActionState.update { it.copy(countDownInitTime = it.countDownInitTime.minusSecond()) }
            }

            UserActionIntent.OnSecondIncrease -> {
                _userActionState.update { it.copy(countDownInitTime = it.countDownInitTime.plusSecond()) }
            }

            UserActionIntent.OnDayDecrease -> {
                _userActionState.update { it.copy(countDownInitTime = it.countDownInitTime.minusDay()) }
            }

            UserActionIntent.OnDayIncrease -> {
                _userActionState.update { it.copy(countDownInitTime = it.countDownInitTime.plusDay()) }
            }

            is UserActionIntent.SetDate -> {
                _userActionState.update { it.copy(countDownInitTime = action.countDownInitTimer) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

}
