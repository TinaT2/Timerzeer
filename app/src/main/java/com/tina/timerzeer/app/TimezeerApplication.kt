package com.tina.timerzeer.app

import android.app.Application
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.tina.timerzeer.di.initKoin
import com.tina.timerzeer.di.timerModule
import com.tina.timerzeer.timer.presentation.TimerMode
import com.tina.timerzeer.timer.presentation.TimerScreenRoot
import com.tina.timerzeer.timer.presentation.TimerViewModel
import com.tina.timerzeer.timer.presentation.fullScreenTimer.FullScreenTimerViewModel
import com.tina.timerzeer.timer.presentation.fullScreenTimer.RootTimerStarted
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.compose.viewmodel.koinViewModel
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

    NavHost(
        navController = navController,
        startDestination = Route.TimerGraph
    ) {
        navigation<Route.TimerGraph>(
            startDestination = Route.Timer
        ) {
            composable<Route.Timer>(
                enterTransition = { fadeIn(animationSpec = tween(durationMillis = (1000))) },
                exitTransition = { fadeOut(animationSpec = tween(durationMillis = (1000))) }
            ) {
                val sharedViewModel = koinViewModel<TimerViewModel>()
                TimerScreenRoot(sharedViewModel) {
                    val userActionState = sharedViewModel.userActionState.value
                    navController.navigate(
                        Route.TimerFullScreen(
                            userActionState.mode,
                            if (userActionState.mode == TimerMode.STOPWATCH)
                                userActionState.timerTitle
                            else userActionState.countdownTitle,
                            userActionState.countDownInitTime
                        )
                    )
                }
            }
            composable<Route.TimerFullScreen>(
                enterTransition = { fadeIn(animationSpec = tween(durationMillis = (1000))) },
                exitTransition = { fadeOut(animationSpec = tween(durationMillis = (1000))) }
            ) {
                val viewModel = koinViewModel<FullScreenTimerViewModel>()

                RootTimerStarted(viewModel) {
                    navController.navigateUp()
                }
            }
        }
    }
}

@Composable
private inline fun <reified T : ViewModel> NavBackStackEntry.sharedKoinViewModel(
    navController: NavController
): T {
    val navGraphRoute = destination.parent?.route ?: return koinViewModel<T>()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return koinViewModel(
        viewModelStoreOwner = parentEntry
    )
}

