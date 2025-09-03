package com.tina.timerzeer.timer.presentation.fullScreenTimer

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.tina.timerzeer.app.Route
import com.tina.timerzeer.core.domain.TimerMode
import com.tina.timerzeer.timer.data.repository.TimerRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class FullScreenTimerViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val repository: TimerRepository,
) : AndroidViewModel(application) {

    companion object {
        const val COUNTDOWN_DONE_DELAY_MS = 3000L
    }

    val args = savedStateHandle.toRoute<Route.TimerFullScreen>()
    private val _timerState = MutableStateFlow(Timer(title = args.title, mode = args.mode))
    val timerState: StateFlow<Timer> = _timerState
    var appearJob: Job? = null

    init {
        if (_timerState.value.mode == TimerMode.COUNTDOWN && args.time != null)
            repository.update(args.time)
        observeTimelapse()
    }

    private fun observeTimelapse() {
        viewModelScope.launch {
            repository.timeFlow.collect { time ->
                _timerState.update { it.copy(elapsedTime = time) }
                if (_timerState.value.mode == TimerMode.COUNTDOWN && time == 0L) {
                        _timerState.update { it.copy(isCountDownDone = true) }
                        onTimerIntent(TimerFullScreenIntent.Stop)
                }
            }
        }
    }


    fun onTimerIntent(intent: TimerFullScreenIntent) {
        when (intent) {
            TimerFullScreenIntent.Start -> {
                if (_timerState.value.isRunning) return
                _timerState.update { it.copy(isRunning = true) }
                val intent = Intent(application, TimerService::class.java)
                intent.apply {
                    action = TimerForegroundActions.ACTION_START.name
                    putExtra(TimerService.ARG_MODE, _timerState.value.mode.name)
                }
                ContextCompat.startForegroundService(application, intent)
            }

            TimerFullScreenIntent.Pause -> {
                val intent = Intent(application, TimerService::class.java).apply {
                    action = TimerForegroundActions.ACTION_PAUSE.name
                }
                application.startService(intent)
                _timerState.update { it.copy(isRunning = false) }
            }

            TimerFullScreenIntent.Resume -> {
                if (!_timerState.value.isRunning) {
                    _timerState.update { it.copy(isRunning = true) }
                    val intent = Intent(application, TimerService::class.java).apply {
                        action = TimerForegroundActions.ACTION_RESUME.name
                    }
                    ContextCompat.startForegroundService(application, intent)
                }
            }

            TimerFullScreenIntent.Stop -> {
                val intent = Intent(application, TimerService::class.java).apply {
                    action = TimerForegroundActions.ACTION_STOP.name
                }
                application.startService(intent)
                _timerState.update { it.copy(isRunning = false) }
            }

            TimerFullScreenIntent.Hide -> {
                viewModelScope.launch {
                    _timerState.update { it.copy(hide = !_timerState.value.hide) }
                    if (timerState.value.hide) {
                        delay(3000)
                        _timerState.update { it.copy(iconAppear = false) }
                    }
                }
            }

            TimerFullScreenIntent.Lock -> {
                viewModelScope.launch {
                    _timerState.update { it.copy(lock = !_timerState.value.lock) }
                    if (timerState.value.lock) {
                        delay(3000)
                        _timerState.update { it.copy(iconAppear = false) }
                    }
                }
            }

            TimerFullScreenIntent.IconAppear -> {
                appearJob?.cancel()
                _timerState.update { it.copy(iconAppear = true) }
                appearJob = viewModelScope.launch {
                    delay(4000)
                    _timerState.update { it.copy(iconAppear = false) }
                }
            }
        }
    }

}
