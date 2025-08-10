package com.tina.timerzeer.ui.stopwatch

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tina.timerzeer.R
import com.tina.timerzeer.core.presentation.components.PrimaryButton
import com.tina.timerzeer.core.presentation.components.TimerInputField
import com.tina.timerzeer.core.presentation.components.TimerOptionRow
import com.tina.timerzeer.core.theme.RoundedCornerShapeNumber
import com.tina.timerzeer.core.theme.SizeS
import com.tina.timerzeer.core.theme.SizeXL
import com.tina.timerzeer.core.theme.SizeXS
import com.tina.timerzeer.core.theme.SizeXXXL
import com.tina.timerzeer.core.theme.TimerzeerTheme
import com.tina.timerzeer.mapper.toTimeComponents
import com.tina.timerzeer.ui.components.LightDarkPreviews
import com.tina.timerzeer.ui.components.SegmentedTab
import com.tina.timerzeer.ui.components.TimeSelector
import org.koin.androidx.compose.koinViewModel


@Composable
fun StopwatchScreenRoot(
    innerPadding: PaddingValues
) {
    val viewModel: StopwatchViewModel = koinViewModel<StopwatchViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val selectedIndex by viewModel.selectedTabIndex.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier
            .padding(innerPadding)
            .background(colorScheme.background)
    ) { paddingValues ->
        StopWatchScreen(
            paddingValues,
            state = state,
            selectedIndex = selectedIndex,
            onIntent = { intent ->
                viewModel.onIntent(intent)
            })
    }

}

@Composable
private fun StopWatchScreen(
    paddingValues: PaddingValues,
    state: StopWatchState,
    selectedIndex: Int,
    onIntent: (StopwatchIntent) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = SizeS),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                    ("Stopwatch" to R.drawable.property_1_clock_stopwatch),
                    ("Countdown" to R.drawable.property_1_clock_fast_forward)
                ), selected = selectedIndex, onSelect = {
//todo
                })
        }
        Spacer(modifier = Modifier.height(SizeXXXL))

        TimerInputField(
            value = state.title, error = state.errorMessage,
            placeholder = stringResource(R.string.stopwatch_title)
        ) {
            onIntent(StopwatchIntent.OnTitleChange(it))
        }

        Spacer(Modifier.height(SizeXL))

        Row(modifier = Modifier.padding(vertical = SizeXXXL)) {
            val time = state.elapsedTime.toTimeComponents()
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

        Spacer(Modifier.weight(1f))

        PrimaryButton(
            text = stringResource(R.string.start),
            onClick = { onIntent(StopwatchIntent.Start) })
    }
}

@LightDarkPreviews
@Composable
fun StopWatchPreview() {
    val stopWatchState = StopWatchState()
    TimerzeerTheme {
        StopWatchScreen(PaddingValues(), stopWatchState, selectedIndex = 0) {}
    }
}
