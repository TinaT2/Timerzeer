package com.tina.timerzeer.app

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.tina.timerzeer.di.initKoin
import com.tina.timerzeer.di.timerModule
import com.tina.timerzeer.ui.stopwatch.RootStopWatchStarted
import com.tina.timerzeer.ui.stopwatch.StopwatchIntent
import com.tina.timerzeer.ui.stopwatch.StopwatchScreenRoot
import com.tina.timerzeer.ui.stopwatch.StopwatchViewModel
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
            startDestination = Route.Stopwatch
        ) {
            composable<Route.Stopwatch> {
                val sharedViewModel = it.sharedKoinViewModel<StopwatchViewModel>(navController)
                StopwatchScreenRoot(sharedViewModel) { route ->
                    navController.navigate(Route.StopwatchStarted)
                }
            }
            composable<Route.StopwatchStarted>() {
                val sharedViewModel = it.sharedKoinViewModel<StopwatchViewModel>(navController)
                RootStopWatchStarted(sharedViewModel) {
                sharedViewModel.onStopwatchIntent(StopwatchIntent.Stop)
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

