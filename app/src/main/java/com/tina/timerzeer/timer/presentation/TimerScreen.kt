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
import com.tina.timerzeer.core.presentation.components.DefaultBottomSheet
import com.tina.timerzeer.core.presentation.components.OutlinedPrimaryButton
import com.tina.timerzeer.core.presentation.components.PrimaryButton
import com.tina.timerzeer.core.presentation.components.SmoothFieldFadeAnimatedVisibility
import com.tina.timerzeer.core.presentation.components.SmoothSwitchTabFadeAnimatedVisibility
import com.tina.timerzeer.core.presentation.components.StyledDatePicker
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
    onTimerStarted: () -> Unit = {}
) {
    val userActionState by viewModel.userActionState.collectAsStateWithLifecycle()
    var uiOverlay: UiOverlay by remember { mutableStateOf(UiOverlay.None) }

    Scaffold(
        modifier = Modifier
            .padding(innerPadding)
            .background(colorScheme.background)
            .padding(top = SizeXL)
    ) { paddingValues ->
        TimerScreen(
            paddingValues,
            userActionState = userActionState,
            onTimerStarted = {
                onTimerStarted()
            },
            onUserActionIntent = { intent ->
                viewModel.onUserAction(intent)
            },
            onStyleChange = { uiOverlay = UiOverlay.TimerStyle },
            onBackgroundThemeChange = { uiOverlay = UiOverlay.BackgroundTheme },
            onEndingAnimationChange = { uiOverlay = UiOverlay.EndingAnimation },
            onShowDatePicker = { uiOverlay = UiOverlay.DatePicker }
        )

        UIOverlays(
            uiOverlay,
            { viewModel.onUserAction(it) },
            onDismiss = { uiOverlay = UiOverlay.None })
    }

}

@Composable
private fun UIOverlays(
    uiOverlay: UiOverlay,
    onUserAction: (UserActionIntent) -> Unit,
    onDismiss: () -> Unit
) {
    when (uiOverlay) {
        UiOverlay.BackgroundTheme -> {
            DefaultBottomSheet(
                title = R.string.background_theme,
                leadingIcon = R.drawable.property_1_image_02,
                optionList = listOf(
                    R.string.background_theme_dark,
                    R.string.background_theme_galaxy,
                    R.string.background_theme_digital
                ),
                onDismiss = {
                    onDismiss()
                }, onStyleSelected = {})
        }

        UiOverlay.DatePicker -> {
            StyledDatePicker(onDateSelected = {
                val now = System.currentTimeMillis()
                val diff = (it - now).coerceAtLeast(0)
                onUserAction(UserActionIntent.SetDate(diff))
            }) {
                onDismiss()
            }
        }

        UiOverlay.EndingAnimation -> {
            DefaultBottomSheet(
                title = R.string.ending_animation,
                leadingIcon = R.drawable.property_1_flash,
                optionList = listOf(
                    R.string.ending_animation_fly_ribbons,
                    R.string.ending_animation_Explosives
                ),
                onDismiss = {
                    onDismiss()
                }, onStyleSelected = {})
        }

        UiOverlay.None -> {
            //Nothing
        }

        UiOverlay.TimerStyle -> {
            DefaultBottomSheet(
                title = R.string.timer_style,
                leadingIcon = R.drawable.property_1_roller_brush,
                optionList = listOf(
                    R.string.timerstyle_classic,
                    R.string.timerstyle_minimal,
                    R.string.timerstyle_digital
                ),
                onDismiss = {
                    onDismiss()
                }, onStyleSelected = {})
        }
    }
}

@LightDarkPreviews
@Composable
private fun UIOverlaysPreview() {
    ThemedPreview {
        UIOverlays(
            uiOverlay = UiOverlay.DatePicker,
            onUserAction = {},
            onDismiss = {}
        )
    }
}

@Composable
private fun TimerScreen(
    paddingValues: PaddingValues,
    onTimerStarted: () -> Unit,
    userActionState: UserActionState,
    onUserActionIntent: (UserActionIntent) -> Unit,
    onStyleChange: () -> Unit = {},
    onBackgroundThemeChange: () -> Unit = {},
    onEndingAnimationChange: () -> Unit = {},
    onShowDatePicker: () -> Unit = {}
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
                        Stopwatch(userActionState, onUserActionIntent)
                    }

                    SmoothSwitchTabFadeAnimatedVisibility(
                        userActionState.mode == TimerMode.COUNTDOWN,
                    ) {
                        Countdown(
                            userActionState,
                            onUserActionIntent
                        ) { onShowDatePicker() }
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
            enabled = if (userActionState.mode == TimerMode.COUNTDOWN) userActionState.countDownInitTime != 0L else true,
            onClick = { onTimerStarted() })
    }
}

@Composable
fun Stopwatch(
    userActionState: UserActionState,
    onUserActionIntent: (UserActionIntent) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TimerInputField(
            value = userActionState.timerTitle, error = userActionState.errorMessage,
            placeholder = stringResource(R.string.stopwatch_title)
        ) {
            onUserActionIntent(UserActionIntent.OnStopwatchTitleChange(it))
        }

        Spacer(Modifier.height(SizeXL))

        Row(
            modifier = Modifier.padding(vertical = SizeXXXL),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val time = 0L.toTimeComponents()
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
    onUserActionIntent: (UserActionIntent) -> Unit,
    onShowDatePicker: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TimerInputField(
            value = userActionState.countdownTitle, error = userActionState.errorMessage,
            placeholder = stringResource(R.string.countdown_title)
        ) {
            onUserActionIntent(UserActionIntent.OnCountDownTitleChange(it))
        }

        Spacer(Modifier.height(SizeXL))

        Row(
            modifier = Modifier.padding(horizontal = SizeXS),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val time = userActionState.countDownInitTime.toTimeComponents()
            SmoothFieldFadeAnimatedVisibility(time.days > 0) {
                TimeSelector(
                    time.days,
                    selectable = userActionState.mode == TimerMode.COUNTDOWN,
                    label = stringResource(R.string.days),
                    onIncrease = { onUserActionIntent(UserActionIntent.OnDayIncrease) },
                    onDecrease = { onUserActionIntent(UserActionIntent.OnDayDecrease) },
                )
            }
            TimeSelector(
                time.hours,
                selectable = userActionState.mode == TimerMode.COUNTDOWN,
                label = stringResource(R.string.hours),
                onIncrease = { onUserActionIntent(UserActionIntent.OnHourIncrease) },
                onDecrease = { onUserActionIntent(UserActionIntent.OnHourDecrease) })
            TimeSelector(
                time.minutes,
                selectable = userActionState.mode == TimerMode.COUNTDOWN,
                label = stringResource(R.string.minutes),
                onIncrease = { onUserActionIntent(UserActionIntent.OnMinutesIncrease) },
                onDecrease = { onUserActionIntent(UserActionIntent.OnMinutesDecrease) })
            TimeSelector(
                time.seconds,
                selectable = userActionState.mode == TimerMode.COUNTDOWN,
                label = stringResource(R.string.seconds),
                onIncrease = { onUserActionIntent(UserActionIntent.OnSecondIncrease) },
                onDecrease = { onUserActionIntent(UserActionIntent.OnSecondDecrease) })
        }
        Spacer(Modifier.height(SizeXL))

        OutlinedPrimaryButton(text = "Set by date", leadingIcon = R.drawable.property_1_calendar) {
            onShowDatePicker()
        }

    }
}

@LightDarkPreviews
@Composable
private fun StopwatchScreenPreview() {
    ThemedPreview {
        TimerScreen(
            paddingValues = PaddingValues(),
            userActionState = UserActionState(
                timerTitle = "Work Session",
                mode = TimerMode.STOPWATCH,
                countDownInitTime = 3661000L,
                errorMessage = null
            ),
            onUserActionIntent = {},
            onTimerStarted = {}
        )
    }
}

@LightDarkPreviews
@Composable
private fun CountdownScreenPreview() {
    ThemedPreview {
        TimerScreen(
            paddingValues = PaddingValues(),
            userActionState = UserActionState(
                timerTitle = "Work Session",
                mode = TimerMode.COUNTDOWN,
                countDownInitTime = 3661000L,
                errorMessage = null
            ),
            onUserActionIntent = {},
            onTimerStarted = {}
        )
    }
}
