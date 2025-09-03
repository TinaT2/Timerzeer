package com.tina.timerzeer.timer.presentation.fullScreenTimer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.util.Log
import androidx.compose.ui.text.toLowerCase
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.tina.timerzeer.R
import com.tina.timerzeer.core.domain.TimerMode
import com.tina.timerzeer.core.domain.util.LocalUtil
import com.tina.timerzeer.timer.data.mapper.toDisplayString
import com.tina.timerzeer.timer.data.mapper.toTimeComponents
import com.tina.timerzeer.timer.data.repository.TimerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit


class TimerService : LifecycleService() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private lateinit var mode: TimerMode

    val repository: TimerRepository by inject()

    private var timerJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        startForegroundService()
        Log.d("MyService", "onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        intent?.run {
            val action = action ?: TimerForegroundActions.NONE.name

            when (TimerForegroundActions.valueOf(action)) {
                TimerForegroundActions.ACTION_START -> {
                    val modeName = getStringExtra(ARG_MODE)
                    mode = TimerMode.valueOf(modeName ?: TimerMode.STOPWATCH.name)
                    startTimer()
                }

                TimerForegroundActions.ACTION_STOP -> stopTimer()
                TimerForegroundActions.ACTION_PAUSE -> {
                    pauseTimer()
                }

                TimerForegroundActions.ACTION_RESUME -> {
                    startTimer()
                }

                TimerForegroundActions.NONE -> {}
            }
        }
        Log.d("MyService", "onStartCommand")
        return START_STICKY
    }

    private fun startTimer() {
        if (timerJob != null) return

        var elapsedTime = repository.timeFlow.value
        timerJob = serviceScope.launch {
            delay(TimeUnit.SECONDS.toMillis(1))
            while (isActive) {
                if (mode == TimerMode.STOPWATCH)
                    elapsedTime += TimeUnit.SECONDS.toMillis(1)
                else
                    elapsedTime -= TimeUnit.SECONDS.toMillis(1)

                repository.update(elapsedTime)
                updateNotification(elapsedTime)
                delay(TimeUnit.SECONDS.toMillis(1))
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
        repository.reset()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun pauseTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    private fun startForegroundService() {
        val channel = NotificationChannel(
            CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW
        )

        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.timer_running))
            .setContentText(getString(R.string.timer_running))
            .setSmallIcon(R.drawable.property_1_clock_stopwatch)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    private fun updateNotification(milliseconds: Long) {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.timer_running))
            .setContentText(
                "${mode.name.toLowerCase(LocalUtil.local)}: ${
                    milliseconds.toTimeComponents().toDisplayString()
                }"
            )
            .setSmallIcon(R.drawable.property_1_clock_stopwatch)
            .build()

        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        const val ARG_MODE = "Mode"
        const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "timer_channel"
        private const val CHANNEL_NAME = "timer_channel"

    }
}


enum class TimerForegroundActions() {
    ACTION_START,
    ACTION_STOP,
    ACTION_PAUSE,
    ACTION_RESUME,
    NONE
}