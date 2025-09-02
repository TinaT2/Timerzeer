package com.tina.timerzeer.timer.presentation.timerPreview

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import com.tina.timerzeer.core.data.dataStore.DataStoreFields
import com.tina.timerzeer.core.domain.TimerMode
import com.tina.timerzeer.core.presentation.components.DefaultBottomSheet
import com.tina.timerzeer.core.presentation.components.LightDarkPreviews
import com.tina.timerzeer.core.presentation.components.OutlinedPrimaryButton
import com.tina.timerzeer.core.presentation.components.PrimaryButton
import com.tina.timerzeer.core.presentation.components.SmoothFieldFadeAnimatedVisibility
import com.tina.timerzeer.core.presentation.components.StyledDatePicker
import com.tina.timerzeer.core.presentation.components.TextOptionButton
import com.tina.timerzeer.core.presentation.components.ThemedPreview
import com.tina.timerzeer.core.presentation.components.TimerInputField
import com.tina.timerzeer.core.presentation.theme.LocalCustomColors
import com.tina.timerzeer.core.presentation.theme.LocalCustomGraphicIds
import com.tina.timerzeer.core.presentation.theme.SizeS
import com.tina.timerzeer.core.presentation.theme.SizeXL
import com.tina.timerzeer.core.presentation.theme.SizeXS
import com.tina.timerzeer.core.presentation.theme.SizeXXXL
import com.tina.timerzeer.core.presentation.theme.backgrounds
import com.tina.timerzeer.core.presentation.theme.endingAnimations
import com.tina.timerzeer.core.presentation.theme.fontStyles
import com.tina.timerzeer.timer.data.mapper.toTimeComponents
import com.tina.timerzeer.timer.presentation.timerPreview.components.SegmentedTab
import com.tina.timerzeer.timer.presentation.timerPreview.components.TimeSelector


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreenRoot(
    viewModel: TimerPreviewViewModel,
    onTimerStarted: () -> Unit = {}
) {
    val timerPreviewState by viewModel.timerPreviewState.collectAsStateWithLifecycle()
    var uiOverlayIntent: UiOverlayIntent by remember { mutableStateOf(UiOverlayIntent.None) }
    val customColors = LocalCustomColors.current
    val customGraphicIds = LocalCustomGraphicIds.current

    Box(modifier = Modifier.fillMaxSize()) {
        backgrounds()[customGraphicIds.backgroundId]?.invoke()

        Scaffold(
            modifier = Modifier
                .background(customColors.mainBackground)
                .padding(top = SizeXL),
            containerColor = customColors.mainBackground
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
    val customGraphicIds = LocalCustomGraphicIds.current
    when (uiOverlayIntent) {
        UiOverlayIntent.BackgroundTheme -> {
            DefaultBottomSheet(
                title = R.string.background_theme,
                selected = customGraphicIds.backgroundId ?: backgrounds().keys.first(),
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
                selected = customGraphicIds.endingAnimationId,
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
                selected = customGraphicIds.fontId,
                leadingIcon = R.drawable.property_1_roller_brush,
                optionList = fontStyles.keys.toList(),
                customListStyle = DataStoreFields.FONT_STYLE,
                onDismiss = {
                    onDismiss()
                }, onItemSelected = {
                    onUserAction(TimerPreviewIntent.SetStyle(it))
                    onDismiss()
                })
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
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                        onSelect = {
                            onUserActionIntent(TimerPreviewIntent.OnModeChange(TimerMode.entries[it]))
                        })

                    Spacer(modifier = Modifier.height(SizeXXXL))

                    TimerInputField(
                        value = timerPreviewState.getTitle(),
                        error = timerPreviewState.errorMessage,
                        placeholder = stringResource(timerPreviewState.getPlaceHolder())
                    ) {
                        onUserActionIntent(timerPreviewState.getOnTitleChange(it))
                    }

                    Spacer(Modifier.height(SizeXL))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val time =
                            if (timerPreviewState.mode == TimerMode.STOPWATCH) 0L.toTimeComponents() else timerPreviewState.countDownInitTime.toTimeComponents()
                        SmoothFieldFadeAnimatedVisibility(time.days > 0) {
                            TimeSelector(
                                time.days,
                                selectable = timerPreviewState.mode == TimerMode.COUNTDOWN,
                                label = stringResource(R.string.days),
                                onIncrease = { onUserActionIntent(TimerPreviewIntent.OnDayIncrease) },
                                onDecrease = { onUserActionIntent(TimerPreviewIntent.OnDayDecrease) },
                            )
                        }

                        TimeSelector(
                            time.hours,
                            selectable = timerPreviewState.mode == TimerMode.COUNTDOWN,
                            label = stringResource(R.string.hours),
                            onIncrease = { onUserActionIntent(TimerPreviewIntent.OnHourIncrease) },
                            onDecrease = { onUserActionIntent(TimerPreviewIntent.OnHourDecrease) })
                        TimeSelector(
                            time.minutes,
                            selectable = timerPreviewState.mode == TimerMode.COUNTDOWN,
                            label = stringResource(R.string.minutes),
                            onIncrease = { onUserActionIntent(TimerPreviewIntent.OnMinutesIncrease) },
                            onDecrease = { onUserActionIntent(TimerPreviewIntent.OnMinutesDecrease) })
                        TimeSelector(
                            time.seconds,
                            selectable = timerPreviewState.mode == TimerMode.COUNTDOWN,
                            label = stringResource(R.string.seconds),
                            onIncrease = { onUserActionIntent(TimerPreviewIntent.OnSecondIncrease) },
                            onDecrease = { onUserActionIntent(TimerPreviewIntent.OnSecondDecrease) })
                    }

                    SmoothFieldFadeAnimatedVisibility(timerPreviewState.mode == TimerMode.COUNTDOWN) {
                        Column {
                            Spacer(Modifier.height(SizeXL))

                            OutlinedPrimaryButton(
                                text = "Set by date",
                                leadingIcon = R.drawable.property_1_calendar
                            ) {
                                onShowDatePicker()
                            }
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

                    SmoothFieldFadeAnimatedVisibility(timerPreviewState.mode == TimerMode.COUNTDOWN) {
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
        }
        PrimaryButton(
            modifier = Modifier.align(Alignment.BottomCenter),
            text = stringResource(R.string.start),
            enabled = if (timerPreviewState.mode == TimerMode.COUNTDOWN) timerPreviewState.countDownInitTime != 0L else true,
            onClick = { onTimerStarted() })
    }
}

@LightDarkPreviews
@Composable
private fun StopwatchScreenPreview() {
    ThemedPreview {
        TimerScreen(
            paddingValues = PaddingValues(),
            timerPreviewState = TimerPreviewState(
                stopwatchTitle = "Work Session",
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
                stopwatchTitle = "Work Session",
                mode = TimerMode.COUNTDOWN,
                countDownInitTime = 3661000L,
                errorMessage = null
            ),
            onUserActionIntent = {},
            onTimerStarted = {}
        )
    }
}
