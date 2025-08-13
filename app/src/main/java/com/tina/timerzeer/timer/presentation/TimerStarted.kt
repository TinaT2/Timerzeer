package com.tina.timerzeer.timer.presentation

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tina.timerzeer.R
import com.tina.timerzeer.core.presentation.components.CaptionTextField
import com.tina.timerzeer.core.presentation.components.HeadlineMediumTextField
import com.tina.timerzeer.core.presentation.components.RoundIconFilledMedium
import com.tina.timerzeer.core.presentation.components.RoundIconOutlinedSmall
import com.tina.timerzeer.core.presentation.components.SmoothFieldFadeAnimatedVisibility
import com.tina.timerzeer.core.theme.SizeS
import com.tina.timerzeer.core.theme.SizeXL
import com.tina.timerzeer.core.theme.SizeXXXL
import com.tina.timerzeer.timer.data.mapper.toTimeComponents
import com.tina.timerzeer.timer.presentation.components.LightDarkPreviews
import com.tina.timerzeer.timer.presentation.components.ThemedPreview
import com.tina.timerzeer.timer.presentation.components.TimeSelector


@Composable
fun RootTimerStarted(viewModel: TimerViewModel, onNavigateBack: () -> Unit) {
    val timerState = viewModel.timerState.collectAsStateWithLifecycle()
    val userActionState = viewModel.userActionState.collectAsStateWithLifecycle()
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    DisposableEffect(Unit) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.onTimerIntent(TimerIntent.Stop)
                onNavigateBack()
            }
        }
        backDispatcher?.addCallback(callback)

        onDispose {
            callback.remove()
        }
    }

    LaunchedEffect(Unit) { viewModel.onTimerIntent(TimerIntent.Start) }
    TimerStarted(timerState.value, userActionState.value, onTimerIntent = {
        viewModel.onTimerIntent(it)
    }, onCountdownIntent = { viewModel.onCountDownIntent(it) }, onNavigateBack = onNavigateBack)
}

@Composable
fun TimerStarted(
    timerState: Timer,
    userActionState: UserActionState,
    onTimerIntent: (TimerIntent) -> Unit,
    onCountdownIntent: (CountDownIntent) -> Unit,
    onNavigateBack: () -> Unit
) {
    var show: Boolean by remember { mutableStateOf(true) }
    LaunchedEffect(timerState.isCountDownDone) {
        if (timerState.isCountDownDone)
            onNavigateBack()
        onCountdownIntent(CountDownIntent.ResetIsCountDownDone)
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.weight(1.3f))

            HeadlineMediumTextField(if (userActionState.mode == TimerMode.COUNTDOWN) userActionState.countdownTitle else userActionState.timerTitle)

            Row(modifier = Modifier.padding(vertical = SizeXXXL)) {
                val time = timerState.elapsedTime.toTimeComponents()
                if (time.hours != 0)
                    TimeSelector(
                        time.hours,
                        selectable = false,
                        label = stringResource(R.string.hours)
                    )
                TimeSelector(
                    time.minutes,
                    selectable = false,
                    label = stringResource(R.string.minutes)
                )
                TimeSelector(
                    time.seconds,
                    selectable = false,
                    label = stringResource(R.string.seconds)
                )
            }

            Spacer(Modifier.weight(1f))
            Box (contentAlignment = Alignment.Center){
                SmoothFieldFadeAnimatedVisibility(visible = show) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RoundIconOutlinedSmall(
                            R.drawable.property_1_eye_off,
                            stringResource(R.string.hide_ui)
                        ) {
                            show = false
                        }
                        Spacer(Modifier.width(SizeXL))
                        RoundIconOutlinedSmall(
                            R.drawable.property_1_stop,
                            stringResource(R.string.stop)
                        ) {
                            onTimerIntent(TimerIntent.Stop)
                            onNavigateBack()
                        }
                        Spacer(Modifier.width(SizeXL))
                        if (timerState.isRunning)
                            RoundIconFilledMedium(
                                R.drawable.property_1_pause_circle,
                                stringResource(R.string.pause)
                            ) {
                                onTimerIntent(TimerIntent.Pause)
                            }
                        else
                            RoundIconFilledMedium(
                                R.drawable.property_1_play,
                                stringResource(R.string.play)
                            ) {
                                onTimerIntent(TimerIntent.Resume)
                            }
                        Spacer(Modifier.width(SizeXL))
                        RoundIconOutlinedSmall(
                            R.drawable.property_1_lock_01,
                            stringResource(R.string.lock)
                        ) {
                            //TODO
                        }
                        Spacer(Modifier.width(SizeXL))
                        RoundIconOutlinedSmall(
                            R.drawable.property_1_share_06,
                            stringResource(R.string.share)
                        ) {
                            //TODO()
                        }
                    }
                }

                SmoothFieldFadeAnimatedVisibility(visible = !show) {
                    RoundIconOutlinedSmall(
                        R.drawable.property_1_eye,
                        stringResource(R.string.show_ui)
                    ) {
                        show = true
                    }
                }
            }


            Spacer(Modifier.weight(1f))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CaptionTextField(stringResource(R.string.powered_by))
                Icon(
                    painter = painterResource(R.drawable.timezeer),
                    modifier = Modifier.height(SizeS),
                    contentDescription = stringResource(
                        R.string.titleIcon
                    ),
                    tint = Color.Unspecified
                )
            }
        }

    }
}

@LightDarkPreviews
@Composable
fun TimerStartedPreview() {
    ThemedPreview {
        val timerState = Timer(
            elapsedTime = 3661000L,
            isRunning = true
        ) // Example: 1 hour, 1 minute, 1 second
        val userActionStateAction =
            UserActionState(timerTitle = "how it could take long to get a \$100 skin")
        TimerStarted(timerState, userActionStateAction, {}, {}, {})
    }

}

