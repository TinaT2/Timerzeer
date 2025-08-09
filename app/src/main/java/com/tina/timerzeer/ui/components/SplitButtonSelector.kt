package com.tina.timerzeer.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.tina.timerzeer.core.theme.SizeM
import com.tina.timerzeer.core.theme.SizeS
import com.tina.timerzeer.core.theme.SizeXXS
import com.tina.timerzeer.core.theme.TimerzeerTheme

@Composable
fun SplitButtonSelector(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Column(
        modifier = Modifier
            .background(colorScheme.background, shape = RoundedCornerShape(SizeM))
            .padding(SizeXXS)
    ) {
        options.forEachIndexed { index, label ->
            val isSelected = index == selectedIndex
            val backgroundColor = if (isSelected) colorScheme.primary else Color.Transparent
            val textColor = if (isSelected) colorScheme.onPrimary else colorScheme.onSurface

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50))
                    .clickable { onSelect(index) }
                    .background(backgroundColor)
                    .padding(vertical = SizeS),
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
fun SplitButtonSelectorPreview() {
    TimerzeerTheme {
        SplitButtonSelector(
            options = listOf("Option 1", "Option 2", "Option 3"),
            selectedIndex = 0,
            onSelect = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SplitButtonSelectorNightPreview() {
    TimerzeerTheme {
        SplitButtonSelector(
            options = listOf("Option 1", "Option 2", "Option 3"),
            selectedIndex = 0,
            onSelect = {}
        )
    }
}


