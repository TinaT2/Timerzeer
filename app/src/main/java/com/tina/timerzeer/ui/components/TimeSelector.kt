package com.tina.timerzeer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tina.timerzeer.core.theme.SizeM
import com.tina.timerzeer.core.theme.SizeS

@Composable
fun TimeSelector(
    value: Int,
    label: String,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ThemedArrowIcon(Icons.Default.KeyboardArrowUp, onIncrease)

        Box(
            modifier = Modifier
                .border(1.dp, colorScheme.surface, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value.toString().padStart(2, '0'),
                color = colorScheme.onPrimary,
                style = typography.headlineLarge,
                modifier = Modifier.padding(horizontal = SizeM, vertical = SizeS)
            )
        }

        ThemedArrowIcon(Icons.Default.KeyboardArrowDown) { onDecrease()}

        Text(
            text = label,
            color = colorScheme.onSurface,
            style = typography.labelLarge
        )
    }
}

@Composable
private fun ThemedArrowIcon(
    imageVector: ImageVector,
    onIncrease: () -> Unit,
) {
    IconButton(
        onClick = onIncrease,
        modifier = Modifier
            .padding(SizeM)
            .background(colorScheme.primary, shape = RoundedCornerShape(50))
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "Increase",
            tint = colorScheme.surface
        )
    }
}

@LightDarkPreviews
@Composable
fun TimeSelectorPreview() {
    ThemedPreview {
        TimeSelector(
            value = 10,
            label = "Minutes",
            onIncrease = {},
            onDecrease = {}
        )
    }
}

