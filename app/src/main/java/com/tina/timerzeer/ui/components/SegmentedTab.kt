package com.tina.timerzeer.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.tina.timerzeer.core.theme.RoundedCornerShapeNumber
import com.tina.timerzeer.core.theme.SizeXS
import com.tina.timerzeer.core.theme.TimerzeerTheme

@Composable
fun SegmentedTab(tabList:List<String>,selected: Int, onSelect: (Int) -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Row(
        modifier = Modifier
            .background(colorScheme.background, shape = RoundedCornerShape(50))
            .padding(SizeXS)
    ) {
        tabList.forEachIndexed { index, label ->
            val isSelected = selected == index
            val bgColor = if (isSelected) colorScheme.primary else Color.Transparent
            val textColor = if (isSelected) colorScheme.surface else colorScheme.onSecondary

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(RoundedCornerShapeNumber))
                    .clickable { onSelect(index) }
                    .background(bgColor)
                    .padding(vertical = SizeXS),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
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
        SegmentedTab(tabList = listOf("Tab 1", "Tab 2"), selected = 0, onSelect = {})
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SegmentedTabNightPreview() {
    TimerzeerTheme {
        SegmentedTab(tabList = listOf("Tab 1", "Tab 2"), selected = 0, onSelect = {})
    }
}

