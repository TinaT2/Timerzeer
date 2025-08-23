package com.tina.timerzeer.timer.presentation.timerPreview

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tina.timerzeer.R
import com.tina.timerzeer.core.domain.TimerMode
import com.tina.timerzeer.core.presentation.components.DefaultBottomSheet
import com.tina.timerzeer.core.presentation.components.LightDarkPreviews
import com.tina.timerzeer.core.presentation.components.OutlinedPrimaryButton
import com.tina.timerzeer.core.presentation.components.PrimaryButton
import com.tina.timerzeer.core.presentation.components.SmoothFieldFadeAnimatedVisibility
import com.tina.timerzeer.core.presentation.components.SmoothSwitchTabFadeAnimatedVisibility
import com.tina.timerzeer.core.presentation.components.StyledDatePicker
import com.tina.timerzeer.core.presentation.components.TextOptionButton
import com.tina.timerzeer.core.presentation.components.ThemedPreview
import com.tina.timerzeer.core.presentation.components.TimerInputField
import com.tina.timerzeer.core.theme.RoundedCornerShapeNumber
import com.tina.timerzeer.core.theme.SizeS
import com.tina.timerzeer.core.theme.SizeXL
import com.tina.timerzeer.core.theme.SizeXS
import com.tina.timerzeer.core.theme.SizeXXXL
import com.tina.timerzeer.core.theme.backgrounds
import com.tina.timerzeer.core.theme.endingAnimations
import com.tina.timerzeer.core.theme.fontStyles
import com.tina.timerzeer.timer.data.mapper.toTimeComponents
import com.tina.timerzeer.timer.presentation.timerPreview.components.SegmentedTab
import com.tina.timerzeer.timer.presentation.timerPreview.components.TimeSelector
import kotlin.collections.get


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreenRoot(
    viewModel: TimerPreviewViewModel,
    onTimerStarted: () -> Unit = {}
) {
    val timerPreviewState by viewModel.timerPreviewState.collectAsStateWithLifecycle()
    var uiOverlayIntent: UiOverlayIntent by remember { mutableStateOf(UiOverlayIntent.None) }
    val bgColor = backgrounds()[timerPreviewState.currentBackground]?.let { Color.Transparent }
        ?: colorScheme.background

    Box(modifier = Modifier.fillMaxSize()) {
        backgrounds()[timerPreviewState.currentBackground]?.invoke()

        Scaffold(
            modifier = Modifier
                .background(bgColor)
                .padding(top = SizeXL),
            containerColor = bgColor
        ) { paddingValues ->
            TimerScreen(
                paddingValues,
                timerPreviewState = timerPreviewState,
                onTimerStarted = {
                    onTimerStarted()
                },
                onUserActionIntent = { intent ->
                    viewModel.onUserAction(intent)
                },
                onStyleChange = { uiOverlayIntent = UiOverlayIntent.TimerStyle },
                onBackgroundThemeChange = { uiOverlayIntent = UiOverlayIntent.BackgroundTheme },
                onEndingAnimationChange = { uiOverlayIntent = UiOverlayIntent.EndingAnimation },
                onShowDatePicker = { uiOverlayIntent = UiOverlayIntent.DatePicker }
            )

            UIOverlays(
                state = timerPreviewState,
                uiOverlayIntent,
                { viewModel.onUserAction(it) },
                onDismiss = { uiOverlayIntent = UiOverlayIntent.None })
        }
    }
}

@Composable
private fun UIOverlays(
    state: TimerPreviewState,
    uiOverlayIntent: UiOverlayIntent,
    onUserAction: (TimerPreviewIntent) -> Unit,
    onDismiss: () -> Unit
) {
    when (uiOverlayIntent) {
        UiOverlayIntent.BackgroundTheme -> {
            DefaultBottomSheet(
                title = R.string.background_theme,
                selected = state.currentBackground ?: backgrounds().keys.first(),
                leadingIcon = R.drawable.property_1_image_02,
                optionList = backgrounds().keys.toList(),
                onDismiss = {
                    onDismiss()
                }, onItemSelected = { backgroundId ->
                    onUserAction(TimerPreviewIntent.SetBackground(backgroundId))
                    onDismiss()
                })
        }

        UiOverlayIntent.DatePicker -> {
            StyledDatePicker(onDateSelected = {
                val now = System.currentTimeMillis()
                val diff = (it - now).coerceAtLeast(0)
                onUserAction(TimerPreviewIntent.SetDate(diff))
            }) {
                onDismiss()
            }
        }

        UiOverlayIntent.EndingAnimation -> {
            DefaultBottomSheet(
                title = R.string.ending_animation,
                selected = state.currentAnimation ?: endingAnimations.keys.first(),
                leadingIcon = R.drawable.property_1_flash,
                optionList = endingAnimations.keys.toList(),
                onDismiss = {
                    onDismiss()
                }, onItemSelected = { nameId ->
                    onUserAction(TimerPreviewIntent.SetEndingAnimation(nameId))
                    onDismiss()
                })
        }

        UiOverlayIntent.None -> {
            //Nothing
        }

        UiOverlayIntent.TimerStyle -> {
            DefaultBottomSheet(
                title = R.string.timer_style,
                selected = state.currentFontStyle ?: fontStyles.keys.first(),
                leadingIcon = R.drawable.property_1_roller_brush,
                optionList = fontStyles.keys.toList(),
                onDismiss = {
                    onDismiss()
                }, onItemSelected = {})
        }
    }
}

@Composable
private fun TimerScreen(
    paddingValues: PaddingValues,
    onTimerStarted: () -> Unit,
    timerPreviewState: TimerPreviewState,
    onUserActionIntent: (TimerPreviewIntent) -> Unit,
    onStyleChange: () -> Unit = {},
    onBackgroundThemeChange: () -> Unit = {},
    onEndingAnimationChange: () -> Unit = {},
    onShowDatePicker: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    val isCustomisedBackground = backgrounds()[timerPreviewState.currentBackground] != null
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

                SegmentedTab(
                    listOf(
                        (TimerMode.STOPWATCH to R.drawable.property_1_clock_stopwatch),
                        (TimerMode.COUNTDOWN to R.drawable.property_1_clock_fast_forward)
                    ), selected = timerPreviewState.mode.ordinal,
                    isCustomisedBackground = isCustomisedBackground,
                    onSelect = {
                        onUserActionIntent(TimerPreviewIntent.OnModeChange(TimerMode.entries[it]))
                    })

                Spacer(modifier = Modifier.height(SizeXXXL))

                Box {
                    SmoothSwitchTabFadeAnimatedVisibility(
                        timerPreviewState.mode == TimerMode.STOPWATCH,
                    ) {
                        Stopwatch(timerPreviewState, isCustomisedBackground, onUserActionIntent)
                    }

                    SmoothSwitchTabFadeAnimatedVisibility(
                        timerPreviewState.mode == TimerMode.COUNTDOWN,
                    ) {
                        Countdown(
                            timerPreviewState,
                            isCustomisedBackground = isCustomisedBackground,
                            onUserActionIntent,
                        ) { onShowDatePicker() }
                    }
                }

                Spacer(Modifier.height(SizeXXXL))

                TextOptionButton(
                    text = stringResource(R.string.value_default),
                    leadingIcon = R.drawable.property_1_roller_brush,
                    trailingIcon = R.drawable.property_1_chevron_right,
                    enabled = false,
                    isCustomisedBackground = isCustomisedBackground
                ) {
                    onStyleChange()
                }
                Spacer(Modifier.height(SizeXS))
                TextOptionButton(
                    text = stringResource(R.string.value_default),
                    leadingIcon = R.drawable.property_1_image_02,
                    trailingIcon = R.drawable.property_1_chevron_right,
                    enabled = false,
                    isCustomisedBackground = isCustomisedBackground
                ) {
                    onBackgroundThemeChange()
                }
                Spacer(Modifier.height(SizeXS))

                SmoothFieldFadeAnimatedVisibility(timerPreviewState.mode == TimerMode.COUNTDOWN) {
                    TextOptionButton(
                        text = stringResource(R.string.value_default),
                        leadingIcon = R.drawable.property_1_flash,
                        trailingIcon = R.drawable.property_1_chevron_right,
                        enabled = false,
                        isCustomisedBackground = isCustomisedBackground
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
            enabled = if (timerPreviewState.mode == TimerMode.COUNTDOWN) timerPreviewState.countDownInitTime != 0L else true,
            onClick = { onTimerStarted() })
    }
}

@Composable
fun Stopwatch(
    timerPreviewState: TimerPreviewState,
    isCustomisedBackground: Boolean,
    onUserActionIntent: (TimerPreviewIntent) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TimerInputField(
            value = timerPreviewState.timerTitle, error = timerPreviewState.errorMessage,
            placeholder = stringResource(R.string.stopwatch_title),
            isCustomisedBackground = isCustomisedBackground
        ) {
            onUserActionIntent(TimerPreviewIntent.OnStopwatchTitleChange(it))
        }

        Spacer(Modifier.height(SizeXL))

        Row(
            modifier = Modifier
                .padding(vertical = SizeXXXL)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val time = 0L.toTimeComponents()
            TimeSelector(
                time.hours,
                selectable = timerPreviewState.mode == TimerMode.COUNTDOWN,
                isCustomisedBackground = isCustomisedBackground,
                label = stringResource(R.string.hours)
            )
            TimeSelector(
                time.minutes,
                selectable = timerPreviewState.mode == TimerMode.COUNTDOWN,
                isCustomisedBackground = isCustomisedBackground,
                label = stringResource(R.string.minutes)
            )
            TimeSelector(
                time.seconds,
                selectable = timerPreviewState.mode == TimerMode.COUNTDOWN,
                isCustomisedBackground = isCustomisedBackground,
                label = stringResource(R.string.seconds)
            )
        }
    }
}

@Composable
private fun Countdown(
    timerPreviewState: TimerPreviewState,
    isCustomisedBackground: Boolean,
    onUserActionIntent: (TimerPreviewIntent) -> Unit,
    onShowDatePicker: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TimerInputField(
            value = timerPreviewState.countdownTitle, error = timerPreviewState.errorMessage,
            placeholder = stringResource(R.string.countdown_title),
            isCustomisedBackground = isCustomisedBackground
        ) {
            onUserActionIntent(TimerPreviewIntent.OnCountDownTitleChange(it))
        }

        Spacer(Modifier.height(SizeXL))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            val time = timerPreviewState.countDownInitTime.toTimeComponents()
            SmoothFieldFadeAnimatedVisibility(time.days > 0) {
                TimeSelector(
                    time.days,
                    selectable = timerPreviewState.mode == TimerMode.COUNTDOWN,
                    label = stringResource(R.string.days),
                    isCustomisedBackground = isCustomisedBackground,
                    onIncrease = { onUserActionIntent(TimerPreviewIntent.OnDayIncrease) },
                    onDecrease = { onUserActionIntent(TimerPreviewIntent.OnDayDecrease) },
                )
            }
            TimeSelector(
                time.hours,
                selectable = timerPreviewState.mode == TimerMode.COUNTDOWN,
                label = stringResource(R.string.hours),
                isCustomisedBackground = isCustomisedBackground,
                onIncrease = { onUserActionIntent(TimerPreviewIntent.OnHourIncrease) },
                onDecrease = { onUserActionIntent(TimerPreviewIntent.OnHourDecrease) })
            TimeSelector(
                time.minutes,
                selectable = timerPreviewState.mode == TimerMode.COUNTDOWN,
                label = stringResource(R.string.minutes),
                isCustomisedBackground = isCustomisedBackground,
                onIncrease = { onUserActionIntent(TimerPreviewIntent.OnMinutesIncrease) },
                onDecrease = { onUserActionIntent(TimerPreviewIntent.OnMinutesDecrease) })
            TimeSelector(
                time.seconds,
                selectable = timerPreviewState.mode == TimerMode.COUNTDOWN,
                label = stringResource(R.string.seconds),
                isCustomisedBackground = isCustomisedBackground,
                onIncrease = { onUserActionIntent(TimerPreviewIntent.OnSecondIncrease) },
                onDecrease = { onUserActionIntent(TimerPreviewIntent.OnSecondDecrease) })
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
            timerPreviewState = TimerPreviewState(
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
            timerPreviewState = TimerPreviewState(
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
