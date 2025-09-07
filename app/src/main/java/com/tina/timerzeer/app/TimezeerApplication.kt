package com.tina.timerzeer.app

import android.app.Application
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.tina.timerzeer.di.initKoin
import com.tina.timerzeer.di.timerModule
import com.tina.timerzeer.timer.data.repository.TimerRepository
import com.tina.timerzeer.timer.presentation.fullScreenTimer.RootTimerFullScreen
import com.tina.timerzeer.timer.presentation.timerPreview.TimerScreenRoot
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.compose.koinInject
import org.koin.core.logger.Level

class TimezeerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@TimezeerApplication)
            androidLogger(Level.DEBUG)
            modules(timerModule)
        }
    }
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val repository: TimerRepository = koinInject()

    NavHost(
        navController = navController,
        startDestination = Route.TimerGraph
    ) {
        navigation<Route.TimerGraph>(
            startDestination = Route.TimerPreview
        ) {
            composable<Route.TimerPreview>(
                enterTransition = { fadeIn(animationSpec = tween(durationMillis = (1000))) },
                exitTransition = { fadeOut(animationSpec = tween(durationMillis = (1000))) }
            ) {
                LaunchedEffect(Unit) {
                    if (repository.timerState.value.isRunning)
                        navController.navigate(
                            Route.TimerFullScreen
                        )
                }
                TimerScreenRoot {
                    navController.navigate(Route.TimerFullScreen)
                }
            }
            composable<Route.TimerFullScreen>(
                enterTransition = { fadeIn(animationSpec = tween(durationMillis = (1000))) },
                exitTransition = { fadeOut(animationSpec = tween(durationMillis = (1000))) }
            ) {

                RootTimerFullScreen {
                    navController.navigateUp()
                }
            }
        }
    }
}


