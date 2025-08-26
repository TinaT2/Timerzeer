package com.tina.timerzeer.timer.presentation.timerPreview.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tina.timerzeer.R
import com.tina.timerzeer.core.domain.TimerMode
import com.tina.timerzeer.core.presentation.theme.LocalCustomColors
import com.tina.timerzeer.core.presentation.theme.RoundedCornerShapeNumber
import com.tina.timerzeer.core.presentation.theme.SizeXS
import com.tina.timerzeer.core.presentation.theme.SizeXXS
import com.tina.timerzeer.core.presentation.theme.TextSecondary
import com.tina.timerzeer.core.presentation.theme.TimerzeerTheme

@Composable
fun SegmentedTab(
    tabList: List<Pair<TimerMode, Int>>,
    selected: Int,
    onSelect: (Int) -> Unit
) {
    val customColors = LocalCustomColors.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                customColors.border,
                shape = RoundedCornerShape(RoundedCornerShapeNumber)
            )
            .padding(SizeXS),
        horizontalArrangement = Arrangement.Center
    ) {
        tabList.forEachIndexed { index, tab ->
            val isSelected = selected == index
            val bgColor = if (isSelected) colorScheme.primary else Color.Transparent
            val textColor = if (isSelected) colorScheme.surface else customColors.textColorDisabled

            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(RoundedCornerShapeNumber))
                    .clickable { onSelect(index) }
                    .background(bgColor)
                    .padding(vertical = SizeXS),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(tab.second),
                    contentDescription = stringResource(tab.first.nameId),
                    tint = textColor
                )
                Spacer(Modifier.width(SizeXXS))
                Text(
                    text = stringResource(tab.first.nameId),
                    color = textColor,
                    style = typography.bodyMedium
                )
            }
        }
    }
}

@Preview
@Composable
fun SegmentedTabPreview() {
    TimerzeerTheme {
        SegmentedTab(
            tabList = listOf(
                (TimerMode.STOPWATCH to R.drawable.property_1_clock_stopwatch),
                (TimerMode.COUNTDOWN to R.drawable.property_1_clock_fast_forward),
            ), selected = 0,  onSelect = {})
    }
}
@Preview
@Composable
fun SegmentedTabCustomizedBgPreview() {
    TimerzeerTheme {
        SegmentedTab(
            tabList = listOf(
                (TimerMode.STOPWATCH to R.drawable.property_1_clock_stopwatch),
                (TimerMode.COUNTDOWN to R.drawable.property_1_clock_fast_forward),
            ), selected = 0, onSelect = {})
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SegmentedTabNightPreview() {
    TimerzeerTheme {
        SegmentedTab(
            tabList = listOf(
                (TimerMode.STOPWATCH to R.drawable.property_1_clock_stopwatch),
                (TimerMode.COUNTDOWN to R.drawable.property_1_clock_fast_forward)
            ), selected = 0, onSelect = {})
    }
}

