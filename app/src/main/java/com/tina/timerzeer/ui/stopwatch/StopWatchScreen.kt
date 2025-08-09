package com.tina.timerzeer.ui.stopwatch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tina.timerzeer.R
import com.tina.timerzeer.mapper.toTimeComponents
import com.tina.timerzeer.core.presentation.components.PrimaryButton
import com.tina.timerzeer.ui.stopwatch.components.SettingRow
import com.tina.timerzeer.ui.stopwatch.components.StopwatchTabBar
import com.tina.timerzeer.ui.stopwatch.components.TimeBlock
import com.tina.timerzeer.core.theme.TimerzeerTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun StopwatchScreenRoot(
    innerPadding: PaddingValues
) {
    val viewModel: StopwatchViewModel = koinViewModel<StopwatchViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    StopWatchScreen(innerPadding, state = state, onIntent = { intent ->
        viewModel.onIntent(intent)
    })
}

@Composable
private fun StopWatchScreen(
    paddingValues: PaddingValues,
    state: StopWatchState,
    onIntent: (StopwatchIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Time")
                }
                withStyle(SpanStyle(color = Color(0xFF009DFF), fontWeight = FontWeight.Bold)) {
                    append("zeer")
                }
            },
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        StopwatchTabBar()

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.title ?: stringResource(R.string.stopwatch_title),
            onValueChange = { onIntent(StopwatchIntent.OnTitleChange(it)) },
            placeholder = { Text(stringResource(R.string.stopwatch_title)) },
            shape = RoundedCornerShape(24.dp),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = RoundedCornerShape(size = 32.dp)
                    )
                    .padding(start = 13.dp, top = 13.5.dp, end = 13.dp, bottom = 13.5.dp)

                    .background(Color(0xFFF1F6F9), shape = RoundedCornerShape(24.dp)),
//todo            colors = TextFieldDefaults.colors(
//                unfocusedBorderColor = Color.Transparent,
//                focusedBorderColor = Color.Transparent,
//                backgroundColor = Color(0xFFF1F6F9)
//            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            val time = state.elapsedTime.toTimeComponents()
            TimeBlock(time = time.hours.toString(), label = "Hours")
            TimeBlock(time = time.minutes.toString(), label = "Minutes")
            TimeBlock(time = time.seconds.toString(), label = "Seconds")
        }

        Spacer(modifier = Modifier.height(32.dp))

        SettingRow(icon = Icons.Default.ShoppingCart, label = "Default") {
            // TODO: Open style sheet
        }
        Spacer(modifier = Modifier.height(12.dp))
        SettingRow(icon = Icons.Default.ShoppingCart, label = "Default") {
            // TODO: Open background sheet
        }

        Spacer(modifier = Modifier.weight(1f))

        PrimaryButton(
            text = stringResource(R.string.start),
            onClick = { onIntent(StopwatchIntent.Start) },
        )

//        Button(
//            onClick = {
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(56.dp),
//            shape = RoundedCornerShape(28.dp),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color(0xFF009DFF)
//            )
//        ) {
//
//        }
    }
}

@Preview
@Composable
fun StopWatchPreview() {
    val stopWatchState = StopWatchState()
    TimerzeerTheme {
        StopWatchScreen(PaddingValues(), stopWatchState) {}
    }
}
