package com.tina.timerzeer.timer.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tina.timerzeer.R
import com.tina.timerzeer.app.Route
import com.tina.timerzeer.core.presentation.components.PrimaryButton
import com.tina.timerzeer.core.presentation.components.TimerInputField
import com.tina.timerzeer.core.presentation.components.TimerOptionRow
import com.tina.timerzeer.core.theme.RoundedCornerShapeNumber
import com.tina.timerzeer.core.theme.SizeS
import com.tina.timerzeer.core.theme.SizeXL
import com.tina.timerzeer.core.theme.SizeXS
import com.tina.timerzeer.core.theme.SizeXXXL
import com.tina.timerzeer.timer.data.mapper.toTimeComponents
import com.tina.timerzeer.timer.presentation.components.LightDarkPreviews
import com.tina.timerzeer.timer.presentation.components.SegmentedTab
import com.tina.timerzeer.timer.presentation.components.ThemedPreview
import com.tina.timerzeer.timer.presentation.components.TimeSelector


@Composable
fun TimerScreenRoot(
    viewModel: TimerViewModel,
    innerPadding: PaddingValues = PaddingValues(),
    onTimerStarted: (Route) -> Unit = {}
) {
    val timerState by viewModel.timerState.collectAsStateWithLifecycle()
    val userActionState by viewModel.userActionState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier
            .padding(innerPadding)
            .background(colorScheme.background)
    ) { paddingValues ->
        TimerScreen(
            paddingValues,
            timerState = timerState,
            userActionState = userActionState,
            onTimerIntent = { intent ->
                if (intent is TimerIntent.Start) {
                    onTimerStarted(Route.TimerStarted)
                } else
                    viewModel.onTimerIntent(intent)
            },
            onUserActionIntent = { intent ->
                viewModel.onUserAction(intent)
            },
            onCountDownIntent = { intent ->
                viewModel.onCountDownIntent(intent)
            }
        )
    }

}

@Composable
private fun TimerScreen(
    paddingValues: PaddingValues,
    timerState: Timer,
    userActionState: UserActionState,
    onTimerIntent: (TimerIntent) -> Unit,
    onUserActionIntent: (UserActionIntent) -> Unit,
    onCountDownIntent: (CountDownIntent) -> Unit
) {
    val focusManager = LocalFocusManager.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = SizeS)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Icon(
                    painter = painterResource(R.drawable.timezeer),
                    modifier = Modifier.fillMaxWidth(),
                    contentDescription = stringResource(
                        R.string.titleicon
                    ),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.height(SizeXXXL))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(SizeXS)
                        .border(
                            1.dp,
                            colorScheme.tertiary,
                            shape = RoundedCornerShape(RoundedCornerShapeNumber)
                        ),
                    horizontalArrangement = Arrangement.Center
                ) {
                    SegmentedTab(
                        listOf(
                            (TimerMode.STOPWATCH to R.drawable.property_1_clock_stopwatch),
                            (TimerMode.COUNTDOWN to R.drawable.property_1_clock_fast_forward)
                        ), selected = userActionState.mode.ordinal, onSelect = {
                            onUserActionIntent(UserActionIntent.OnModeChange(TimerMode.entries[it]))
                        })
                }
                Spacer(modifier = Modifier.height(SizeXXXL))

                if (userActionState.mode == TimerMode.STOPWATCH)
                    Stopwatch(userActionState, timerState, onUserActionIntent)
                else
                    Countdown(
                        userActionState,
                        timerState,
                        onUserActionIntent,
                        onCountDownIntent
                    )

                Spacer(Modifier.height(100.dp))
            }
        }
        PrimaryButton(
            modifier = Modifier.align(Alignment.BottomCenter),
            text = stringResource(R.string.start),
            enabled = if (userActionState.mode == TimerMode.COUNTDOWN) timerState.countDownInitTime != 0L else true,
            onClick = { onTimerIntent(TimerIntent.Start) })
    }
}

@Composable
private fun Stopwatch(
    userActionState: UserActionState,
    timerState: Timer,
    onUserActionIntent: (UserActionIntent) -> Unit
) {
    TimerInputField(
        value = userActionState.timerTitle, error = timerState.errorMessage,
        placeholder = stringResource(R.string.stopwatch_title)
    ) {
        onUserActionIntent(UserActionIntent.OnStopwatchTitleChange(it))
    }

    Spacer(Modifier.height(SizeXL))

    Row(modifier = Modifier.padding(vertical = SizeXXXL)) {
        val time = timerState.elapsedTime.toTimeComponents()
        TimeSelector(time.hours, selectable = false, label = stringResource(R.string.hours))
        TimeSelector(time.minutes, selectable = false, label = stringResource(R.string.minutes))
        TimeSelector(time.seconds, selectable = false, label = stringResource(R.string.seconds))
    }
    Spacer(Modifier.height(SizeXXXL))

    TimerOptionRow(
        text = stringResource(R.string.value_default),
        leadingIcon = R.drawable.property_1_roller_brush,
        trailingIcon = R.drawable.property_1_chevron_right
    ) {
        //TODO()
    }
    Spacer(Modifier.height(SizeXS))
    TimerOptionRow(
        text = stringResource(R.string.value_default),
        leadingIcon = R.drawable.property_1_image_02,
        trailingIcon = R.drawable.property_1_chevron_right
    ) {
        //TODO()
    }
}

@Composable
private fun Countdown(
    userActionState: UserActionState,
    timerState: Timer,
    onUserActionIntent: (UserActionIntent) -> Unit,
    onCountDownIntent: (CountDownIntent) -> Unit
) {
    TimerInputField(
        value = userActionState.countdownTitle, error = timerState.errorMessage,
        placeholder = stringResource(R.string.countdown_title)
    ) {
        onUserActionIntent(UserActionIntent.OnCountDownTitleChange(it))
    }

    Spacer(Modifier.height(SizeXL))

    Row(modifier = Modifier.padding(vertical = SizeXXXL)) {
        val time = timerState.countDownInitTime.toTimeComponents()
        TimeSelector(
            time.hours,
            selectable = true,
            label = stringResource(R.string.hours),
            onIncrease = { onCountDownIntent(CountDownIntent.OnHourIncrease) },
            onDecrease = { onCountDownIntent(CountDownIntent.OnHourDecrease) })
        TimeSelector(
            time.minutes,
            selectable = true,
            label = stringResource(R.string.minutes),
            onIncrease = { onCountDownIntent(CountDownIntent.OnMinutesIncrease) },
            onDecrease = { onCountDownIntent(CountDownIntent.OnMinutesDecrease) })
        TimeSelector(
            time.seconds,
            selectable = true,
            label = stringResource(R.string.seconds),
            onIncrease = { onCountDownIntent(CountDownIntent.OnSecondIncrease) },
            onDecrease = { onCountDownIntent(CountDownIntent.OnSecondDecrease) })
    }
    Spacer(Modifier.height(SizeXXXL))

    TimerOptionRow(
        text = stringResource(R.string.value_default),
        leadingIcon = R.drawable.property_1_roller_brush,
        trailingIcon = R.drawable.property_1_chevron_right
    ) {
        //TODO()
    }
    Spacer(Modifier.height(SizeXS))
    TimerOptionRow(
        text = stringResource(R.string.value_default),
        leadingIcon = R.drawable.property_1_image_02,
        trailingIcon = R.drawable.property_1_chevron_right
    ) {
        //TODO()
    }
}

@LightDarkPreviews
@Composable
private fun TimerhScreenPreview() {
    ThemedPreview {
        TimerScreen(
            paddingValues = PaddingValues(),
            timerState = Timer(
                elapsedTime = 3661000L, // 1 hour, 1 minute, 1 second
                isRunning = false,
                errorMessage = null
            ),
            userActionState = UserActionState(
                timerTitle = "Work Session",
                mode = TimerMode.STOPWATCH
            ),
            onTimerIntent = {},
            onUserActionIntent = {},
            onCountDownIntent = {}
        )
    }
}
