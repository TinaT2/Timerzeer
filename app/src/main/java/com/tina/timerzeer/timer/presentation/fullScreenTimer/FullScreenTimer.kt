package com.tina.timerzeer.timer.presentation.fullScreenTimer

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tina.timerzeer.R
import com.tina.timerzeer.core.domain.TimerMode
import com.tina.timerzeer.core.domain.util.LocalUtil
import com.tina.timerzeer.core.domain.util.shareText
import com.tina.timerzeer.core.presentation.components.CaptionTextField
import com.tina.timerzeer.core.presentation.components.HeadlineMediumTextField
import com.tina.timerzeer.core.presentation.components.LightDarkPreviews
import com.tina.timerzeer.core.presentation.components.RoundIconFilledMedium
import com.tina.timerzeer.core.presentation.components.RoundIconOutlinedSmall
import com.tina.timerzeer.core.presentation.components.SmoothFieldFadeAnimatedVisibility
import com.tina.timerzeer.core.presentation.components.ThemedPreview
import com.tina.timerzeer.core.presentation.theme.LocalCustomGraphicIds
import com.tina.timerzeer.core.presentation.theme.SizeS
import com.tina.timerzeer.core.presentation.theme.SizeXL
import com.tina.timerzeer.core.presentation.theme.backgrounds
import com.tina.timerzeer.core.presentation.theme.endingAnimations
import com.tina.timerzeer.timer.data.mapper.toDisplayString
import com.tina.timerzeer.timer.data.mapper.toTimeComponents
import com.tina.timerzeer.timer.presentation.fullScreenTimer.FullScreenTimerViewModel.Companion.COUNTDOWN_DONE_DELAY_MS
import com.tina.timerzeer.timer.presentation.fullScreenTimer.components.LottieLoader
import com.tina.timerzeer.timer.presentation.timerPreview.components.TimeSelector
import kotlinx.coroutines.delay


@Composable
fun RootTimerStarted(viewModel: FullScreenTimerViewModel, onNavigateBack: () -> Unit) {
    val timerState = viewModel.timerState.collectAsStateWithLifecycle()
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    DisposableEffect(Unit) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.onTimerIntent(TimerFullScreenIntent.Stop)
                onNavigateBack()
            }
        }
        backDispatcher?.addCallback(callback)

        onDispose {
            callback.remove()
        }
    }

    LaunchedEffect(Unit) { viewModel.onTimerIntent(TimerFullScreenIntent.Start) }
    TimerStarted(timerState.value, onTimerIntent = {
        viewModel.onTimerIntent(it)
    }, onNavigateBack = onNavigateBack)
}

@Composable
fun TimerStarted(
    timerState: Timer,
    onTimerIntent: (TimerFullScreenIntent) -> Unit,
    onNavigateBack: () -> Unit
) {
    var show: Boolean by remember { mutableStateOf(true) }
    val backgroundComponent = LocalCustomGraphicIds.current.backgroundId
    val customGraphicIds = LocalCustomGraphicIds.current
    val context = LocalContext.current


    LaunchedEffect(timerState.isCountDownDone) {
        if (timerState.isCountDownDone) {
            delay(COUNTDOWN_DONE_DELAY_MS)
            onNavigateBack()
        }
    }

    val bgColor = backgroundComponent?.let { Color.Transparent }
        ?: colorScheme.background

    Box(modifier = Modifier.fillMaxSize()) {
        backgrounds()[backgroundComponent]?.invoke()

        Scaffold(
            modifier = Modifier
                .background(bgColor)
                .padding(top = SizeXL),
            containerColor = bgColor
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(Modifier.weight(1.3f))

                HeadlineMediumTextField(timerState.title)

                Box(
                    modifier = Modifier
                        .weight(2f) // the space you already give to timer
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    SmoothFieldFadeAnimatedVisibility(
                        visible = timerState.isCountDownDone
                    ) {
                        LottieLoader(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .background(colorScheme.background.copy(alpha = 0.3f))
                                .zIndex(2f),
                            resId = endingAnimations[customGraphicIds.endingAnimationId]
                                ?: endingAnimations[R.string.value_default]!!
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        val time = timerState.elapsedTime.toTimeComponents()
                        SmoothFieldFadeAnimatedVisibility(time.days != 0L) {
                            TimeSelector(
                                time.days,
                                selectable = false,
                                label = stringResource(R.string.days)
                            )
                        }
                        SmoothFieldFadeAnimatedVisibility(time.hours != 0L) {
                            TimeSelector(
                                time.hours,
                                selectable = false,
                                label = stringResource(R.string.hours)
                            )
                        }

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
                }

                Spacer(Modifier.weight(1f))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.weight(2f)
                ) {
                    SmoothFieldFadeAnimatedVisibility(visible = show) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RoundIconOutlinedSmall(
                                R.drawable.property_1_eye_off,
                                stringResource(R.string.hide_ui),
                                enabled = timerState.elapsedTime != 0L
                            ) {
                                show = false
                            }
                            Spacer(Modifier.width(SizeXL))
                            RoundIconOutlinedSmall(
                                R.drawable.property_1_stop,
                                stringResource(R.string.stop),
                                enabled = timerState.elapsedTime != 0L
                            ) {
                                onTimerIntent(TimerFullScreenIntent.Stop)
                                onNavigateBack()
                            }
                            Spacer(Modifier.width(SizeXL))
                            if (timerState.isRunning)
                                RoundIconFilledMedium(
                                    R.drawable.property_1_pause_circle,
                                    stringResource(R.string.pause),
                                    enabled = timerState.elapsedTime != 0L
                                ) {
                                    onTimerIntent(TimerFullScreenIntent.Pause)
                                }
                            else
                                RoundIconFilledMedium(
                                    R.drawable.property_1_play,
                                    stringResource(R.string.play),
                                    enabled = timerState.elapsedTime != 0L
                                ) {
                                    onTimerIntent(TimerFullScreenIntent.Resume)
                                }
                            Spacer(Modifier.width(SizeXL))
                            RoundIconOutlinedSmall(
                                R.drawable.property_1_lock_01,
                                stringResource(R.string.lock),
                                enabled = timerState.elapsedTime != 0L
                            ) {
                                //TODO
                            }
                            Spacer(Modifier.width(SizeXL))
                            RoundIconOutlinedSmall(
                                R.drawable.property_1_share_06,
                                stringResource(R.string.share),
                                enabled = timerState.elapsedTime != 0L
                            ) {
                                val shareText =
                                    context.getString(
                                        R.string.my_timer_state,
                                        context.getString(timerState.mode.nameId)
                                            .toLowerCase(LocalUtil.local),
                                        timerState.elapsedTime.toTimeComponents().toDisplayString()
                                    )

                                context.shareText(shareText)
                            }
                        }
                    }

                    SmoothFieldFadeAnimatedVisibility(visible = !show) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RoundIconOutlinedSmall(
                                R.drawable.property_1_eye,
                                stringResource(R.string.show_ui),
                                enabled = timerState.elapsedTime != 0L
                            ) {
                                show = true
                            }
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
}

@LightDarkPreviews
@Composable
fun TimerStartedPreview() {
    ThemedPreview {
        val timerState = Timer(
            elapsedTime = 3661000L,
            isRunning = true,
            title = "Title",
            mode = TimerMode.STOPWATCH
        )
        TimerStarted(timerState, {}, {})
    }

}

