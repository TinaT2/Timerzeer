package com.tina.timerzeer.ui.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
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
import com.tina.timerzeer.core.theme.SizeS
import com.tina.timerzeer.core.theme.SizeXL
import com.tina.timerzeer.core.theme.SizeXXXL
import com.tina.timerzeer.mapper.toTimeComponents
import com.tina.timerzeer.ui.components.LightDarkPreviews
import com.tina.timerzeer.ui.components.ThemedPreview
import com.tina.timerzeer.ui.components.TimeSelector


@Composable
fun RootTimerStarted(viewModel: TimerViewModel, onStop: (Timer) -> Unit) {
    val stopwatchState = viewModel.stopwatchState.collectAsStateWithLifecycle()
    val userActionState = viewModel.userActionState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) { viewModel.onTimerIntent(TimerIntent.Start) }
    TimerStarted(stopwatchState.value, userActionState.value, {
        if (it is TimerIntent.Stop) onStop(stopwatchState.value)
        viewModel.onTimerIntent(it)
    })
}

@Composable
fun TimerStarted(
    stopWatchState: Timer,
    userActionState: UserActionState,
    onIntent: (TimerIntent) -> Unit
) {
    var show: Boolean by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.weight(1.3f))

        HeadlineMediumTextField(userActionState.stopwatchTitle)

        Row(modifier = Modifier.padding(vertical = SizeXXXL)) {
            val time = stopWatchState.elapsedTime.toTimeComponents()
            if (time.hours != 0)
                TimeSelector(time.hours, selectable = false, label = stringResource(R.string.hours))
            TimeSelector(time.minutes, selectable = false, label = stringResource(R.string.minutes))
            TimeSelector(time.seconds, selectable = false, label = stringResource(R.string.seconds))
        }

        Spacer(Modifier.weight(1f))

        if (show) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RoundIconOutlinedSmall(
                    R.drawable.property_1_eye_off,
                    stringResource(R.string.hide_ui)
                ) {
                    show = false
                }
                Spacer(Modifier.width(SizeXL))
                RoundIconOutlinedSmall(R.drawable.property_1_stop, stringResource(R.string.stop)) {
                    onIntent(TimerIntent.Stop)
                }
                Spacer(Modifier.width(SizeXL))
                RoundIconFilledMedium(
                    R.drawable.property_1_pause_circle,
                    stringResource(R.string.pause)
                ) {
                    onIntent(TimerIntent.Pause)
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
        } else {
            RoundIconOutlinedSmall(
                R.drawable.property_1_eye_off,
                stringResource(R.string.show_ui)
            ) {
                show = true
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
                    R.string.titleicon
                ),
                tint = Color.Unspecified
            )
        }
    }
}

@LightDarkPreviews
@Composable
fun StopwatchStartedPreview() {
    ThemedPreview {
        val stopWatchState = Timer(
            elapsedTime = 3661000L,
            isRunning = true
        ) // Example: 1 hour, 1 minute, 1 second
        val userActionStateAction =
            UserActionState(stopwatchTitle = "how it could take long to get a \$100 skin")
        TimerStarted(stopWatchState, userActionStateAction) {}
    }

}

