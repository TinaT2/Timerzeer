package com.tina.timerzeer.timer.data.repository

import android.content.Intent
import androidx.core.content.ContextCompat
import com.tina.timerzeer.app.TimezeerApplication
import com.tina.timerzeer.core.domain.TimerMode
import com.tina.timerzeer.timer.presentation.fullScreenTimer.TimerState
import com.tina.timerzeer.timer.presentation.fullScreenTimer.TimerForegroundActions
import com.tina.timerzeer.timer.presentation.fullScreenTimer.TimerIntent
import com.tina.timerzeer.timer.presentation.fullScreenTimer.TimerService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class TimerRepository(private val application: TimezeerApplication) {

    init {
        val intent = Intent(application, TimerService::class.java)
        ContextCompat.startForegroundService(application, intent)
    }

    private val _timerState = MutableStateFlow(TimerState())
    val timerState: StateFlow<TimerState> = _timerState

    fun update(seconds: Long) {
        if (timerState.value.mode == TimerMode.COUNTDOWN && seconds == 0L) {
            _timerState.update { it.copy(elapsedTime = seconds, isCountDownDone = true) }
            onTimerIntent(TimerIntent.Stop)
        } else
            _timerState.update { it.copy(elapsedTime = seconds) }
    }

    fun update(mode: TimerMode, title: String, initialTime: Long?) {
        _timerState.update {
            it.copy(mode = mode, title = title, initialTime = initialTime)
        }
    }

    fun reset() {
        _timerState.update { it.copy(elapsedTime = 0L) }
    }


    fun onTimerIntent(intent: TimerIntent?) {
        when (intent) {
            TimerIntent.Start -> {
                if (_timerState.value.isRunning) return
                _timerState.update {
                    it.copy(
                        isRunning = true,
                        elapsedTime = _timerState.value.initialTime ?: 0L
                    )
                }
                val intent = Intent(application, TimerService::class.java)
                intent.apply {
                    action = TimerForegroundActions.ACTION_START.name
                }

                application.startService(intent)
            }

            TimerIntent.Pause -> {
                val intent = Intent(application, TimerService::class.java).apply {
                    action = TimerForegroundActions.ACTION_PAUSE.name
                }
                application.startService(intent)
                _timerState.update { it.copy(isRunning = false) }
            }

            TimerIntent.Resume -> {
                if (!_timerState.value.isRunning) {
                    _timerState.update { it.copy(isRunning = true) }
                    val intent = Intent(application, TimerService::class.java).apply {
                        action = TimerForegroundActions.ACTION_RESUME.name
                    }
                    ContextCompat.startForegroundService(application, intent)
                }
            }

            TimerIntent.Stop -> {
                val intent = Intent(application, TimerService::class.java).apply {
                    action = TimerForegroundActions.ACTION_STOP.name
                }
                application.startService(intent)
                _timerState.update { it.copy(isRunning = false) }
            }

            null -> {}
        }
    }
}