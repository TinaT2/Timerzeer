package com.tina.timerzeer.timer.presentation.fullScreenTimer

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.tina.timerzeer.app.Route
import com.tina.timerzeer.core.data.dataStore.DataStoreFields
import com.tina.timerzeer.core.data.repository.SettingsRepository
import com.tina.timerzeer.core.domain.TimerMode
import com.tina.timerzeer.core.presentation.theme.endingAnimations
import com.tina.timerzeer.core.presentation.theme.fontStyles
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


class FullScreenTimerViewModel(savedStateHandle: SavedStateHandle, repository: SettingsRepository) :
    ViewModel() {

    companion object {
        const val COUNTDOWN_DONE_DELAY_MS = 3000L
    }

    val args = savedStateHandle.toRoute<Route.TimerFullScreen>()
    private val _timerState = MutableStateFlow(Timer(title = args.title, mode = args.mode))
    val timerState: StateFlow<Timer> = _timerState

    private var timerJob: Job? = null

    fun onTimerIntent(intent: TimerFullScreenIntent) {
        when (intent) {
            TimerFullScreenIntent.Start -> {
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

            TimerFullScreenIntent.Pause -> {
                timerJob?.cancel()
                _timerState.update { it.copy(isRunning = false) }
            }

            TimerFullScreenIntent.Resume -> {
                if (!_timerState.value.isRunning) {
                    _timerState.update { it.copy(isRunning = true) }
                    startTimer()
                }
            }

            TimerFullScreenIntent.Stop -> {
                timerJob?.cancel()
                _timerState.update { it.copy(elapsedTime = 0L, isRunning = false) }
            }

            TimerFullScreenIntent.Tick -> {
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
            _timerState.update { it.copy(isCountDownDone = true,  isRunning = false) }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000)
                onTimerIntent(TimerFullScreenIntent.Tick)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

}
