package com.tina.timerzeer.timer.presentation.fullScreenTimer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tina.timerzeer.timer.data.repository.TimerRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class FullScreenTimerViewModel(
    application: Application,
    private val repository: TimerRepository,
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(TimerUiState())
    val fullState: StateFlow<FullScreenTimerState> =
        combine(repository.timerState, _uiState) { timer, ui ->
            FullScreenTimerState(timer, ui)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = FullScreenTimerState(Timer(), TimerUiState())
        )

    companion object {
        const val COUNTDOWN_DONE_DELAY_MS = 3000L
    }

    var appearJob: Job? = null

    fun onTimerIntent(intent: TimerFullScreenIntent) {
        when (intent) {
            TimerFullScreenIntent.Start,
            TimerFullScreenIntent.Pause,
            TimerFullScreenIntent.Resume,
            TimerFullScreenIntent.Stop -> {
                repository.onTimerIntent(intent.toTimerIntent())
            }

            TimerFullScreenIntent.Hide -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(hide = !_uiState.value.hide) }
                    if (_uiState.value.hide) {
                        delay(3000)
                        _uiState.update { it.copy(iconAppear = false) }
                    }
                }
            }

            TimerFullScreenIntent.Lock -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(lock = !_uiState.value.lock) }
                    if (_uiState.value.lock) {
                        delay(3000)
                        _uiState.update { it.copy(iconAppear = false) }
                    }
                }
            }

            TimerFullScreenIntent.IconAppear -> {
                appearJob?.cancel()
                _uiState.update { it.copy(iconAppear = true) }
                appearJob = viewModelScope.launch {
                    delay(4000)
                    _uiState.update { it.copy(iconAppear = false) }
                }
            }
        }
    }

}
