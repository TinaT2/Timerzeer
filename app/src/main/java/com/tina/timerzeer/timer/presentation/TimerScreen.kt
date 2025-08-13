package com.tina.timerzeer.timer.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.tina.timerzeer.core.presentation.components.DefaultBottomSheet
import com.tina.timerzeer.core.presentation.components.OutlinedPrimaryButton
import com.tina.timerzeer.core.presentation.components.PrimaryButton
import com.tina.timerzeer.core.presentation.components.SmoothFieldFadeAnimatedVisibility
import com.tina.timerzeer.core.presentation.components.SmoothSwitchTabFadeAnimatedVisibility
import com.tina.timerzeer.core.presentation.components.TextOptionButton
import com.tina.timerzeer.core.presentation.components.TimerInputField
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreenRoot(
    viewModel: TimerViewModel,
    innerPadding: PaddingValues = PaddingValues(),
    onTimerStarted: (Route) -> Unit = {}
) {
    val timerState by viewModel.timerState.collectAsStateWithLifecycle()
    val userActionState by viewModel.userActionState.collectAsStateWithLifecycle()

    var showTimerStyleBottomSheet by remember { mutableStateOf(false) }
    var showBackgroundThemeBottomSheet by remember { mutableStateOf(false) }
    var showEndingAnimationBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .padding(innerPadding)
            .padding(top = SizeXL)
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
            },
            onStyleChange = { showTimerStyleBottomSheet = true },
            onBackgroundThemeChange = { showBackgroundThemeBottomSheet = true },
            onEndingAnimationChange = { showEndingAnimationBottomSheet = true }
        )

        if (showTimerStyleBottomSheet) {
            DefaultBottomSheet(
                title = R.string.timer_style,
                leadingIcon = R.drawable.property_1_roller_brush,
                optionList = listOf(
                    R.string.timerstyle_classic,
                    R.string.timerstyle_minimal,
                    R.string.timerstyle_digital
                ),
                onDismiss = {
                    showTimerStyleBottomSheet = false
                }, onStyleSelected = {})
        }
        if (showBackgroundThemeBottomSheet) {
            DefaultBottomSheet(
                title = R.string.background_theme,
                leadingIcon = R.drawable.property_1_image_02,
                optionList = listOf(
                    R.string.background_theme_dark,
                    R.string.background_theme_galaxy,
                    R.string.background_theme_digital
                ),
                onDismiss = {
                    showBackgroundThemeBottomSheet = false
                }, onStyleSelected = {})
        }
        if (showEndingAnimationBottomSheet) {
            DefaultBottomSheet(
                title = R.string.ending_animation,
                leadingIcon = R.drawable.property_1_flash,
                optionList = listOf(
                    R.string.ending_animation_fly_ribbons,
                    R.string.ending_animation_Explosives
                ),
                onDismiss = {
                    showEndingAnimationBottomSheet = false
                }, onStyleSelected = {})
        }

    }

}

@Composable
private fun TimerScreen(
    paddingValues: PaddingValues,
    timerState: Timer,
    userActionState: UserActionState,
    onTimerIntent: (TimerIntent) -> Unit,
    onUserActionIntent: (UserActionIntent) -> Unit,
    onCountDownIntent: (CountDownIntent) -> Unit,
    onStyleChange: () -> Unit = {},
    onBackgroundThemeChange: () -> Unit = {},
    onEndingAnimationChange: () -> Unit = {},
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
                        R.string.titleIcon
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

                Box {
                    SmoothSwitchTabFadeAnimatedVisibility(
                        userActionState.mode == TimerMode.STOPWATCH,
                    ) {
                        Stopwatch(userActionState, timerState, onUserActionIntent)
                    }

                    SmoothSwitchTabFadeAnimatedVisibility(
                        userActionState.mode == TimerMode.COUNTDOWN,
                    ) {
                        Countdown(
                            userActionState,
                            timerState,
                            onUserActionIntent,
                            onCountDownIntent
                        )
                    }
                }

                Spacer(Modifier.height(SizeXXXL))

                TextOptionButton(
                    text = stringResource(R.string.value_default),
                    leadingIcon = R.drawable.property_1_roller_brush,
                    trailingIcon = R.drawable.property_1_chevron_right,
                    enabled = false
                ) {
                    onStyleChange()
                }
                Spacer(Modifier.height(SizeXS))
                TextOptionButton(
                    text = stringResource(R.string.value_default),
                    leadingIcon = R.drawable.property_1_image_02,
                    trailingIcon = R.drawable.property_1_chevron_right,
                    enabled = false
                ) {
                    onBackgroundThemeChange()
                }
                Spacer(Modifier.height(SizeXS))

                SmoothFieldFadeAnimatedVisibility(userActionState.mode == TimerMode.COUNTDOWN) {
                    TextOptionButton(
                        text = stringResource(R.string.value_default),
                        leadingIcon = R.drawable.property_1_flash,
                        trailingIcon = R.drawable.property_1_chevron_right,
                        enabled = false
                    ) {
                        onEndingAnimationChange()
                    }
                }

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
fun Stopwatch(
    userActionState: UserActionState,
    timerState: Timer,
    onUserActionIntent: (UserActionIntent) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TimerInputField(
            value = userActionState.timerTitle, error = timerState.errorMessage,
            placeholder = stringResource(R.string.stopwatch_title)
        ) {
            onUserActionIntent(UserActionIntent.OnStopwatchTitleChange(it))
        }

        Spacer(Modifier.height(SizeXL))

        Row(modifier = Modifier.padding(vertical = SizeXXXL)) {
            val time = timerState.elapsedTime.toTimeComponents()
            TimeSelector(
                time.hours,
                selectable = userActionState.mode == TimerMode.COUNTDOWN,
                label = stringResource(R.string.hours)
            )
            TimeSelector(
                time.minutes,
                selectable = userActionState.mode == TimerMode.COUNTDOWN,
                label = stringResource(R.string.minutes)
            )
            TimeSelector(
                time.seconds,
                selectable = userActionState.mode == TimerMode.COUNTDOWN,
                label = stringResource(R.string.seconds)
            )
        }
    }
}

@Composable
private fun Countdown(
    userActionState: UserActionState,
    timerState: Timer,
    onUserActionIntent: (UserActionIntent) -> Unit,
    onCountDownIntent: (CountDownIntent) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TimerInputField(
            value = userActionState.countdownTitle, error = timerState.errorMessage,
            placeholder = stringResource(R.string.countdown_title)
        ) {
            onUserActionIntent(UserActionIntent.OnCountDownTitleChange(it))
        }

        Spacer(Modifier.height(SizeXL))

        Row {
            val time = timerState.countDownInitTime.toTimeComponents()
            TimeSelector(
                time.hours,
                selectable = userActionState.mode == TimerMode.COUNTDOWN,
                label = stringResource(R.string.hours),
                onIncrease = { onCountDownIntent(CountDownIntent.OnHourIncrease) },
                onDecrease = { onCountDownIntent(CountDownIntent.OnHourDecrease) })
            TimeSelector(
                time.minutes,
                selectable = userActionState.mode == TimerMode.COUNTDOWN,
                label = stringResource(R.string.minutes),
                onIncrease = { onCountDownIntent(CountDownIntent.OnMinutesIncrease) },
                onDecrease = { onCountDownIntent(CountDownIntent.OnMinutesDecrease) })
            TimeSelector(
                time.seconds,
                selectable = userActionState.mode == TimerMode.COUNTDOWN,
                label = stringResource(R.string.seconds),
                onIncrease = { onCountDownIntent(CountDownIntent.OnSecondIncrease) },
                onDecrease = { onCountDownIntent(CountDownIntent.OnSecondDecrease) })
        }
        Spacer(Modifier.height(SizeXL))

        OutlinedPrimaryButton(text = "Set by date", leadingIcon = R.drawable.property_1_calendar) {
            //TODO()
        }

    }
}

@LightDarkPreviews
@Composable
private fun StopwatchScreenPreview() {
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

@LightDarkPreviews
@Composable
private fun CountdownScreenPreview() {
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
                mode = TimerMode.COUNTDOWN
            ),
            onTimerIntent = {},
            onUserActionIntent = {},
            onCountDownIntent = {}
        )
    }
}
