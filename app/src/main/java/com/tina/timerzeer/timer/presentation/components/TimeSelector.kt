package com.tina.timerzeer.timer.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.tina.timerzeer.core.theme.SizeM
import com.tina.timerzeer.core.theme.SizeS

@Composable
fun TimeSelector(
    value: Long,
    label: String,
    selectable: Boolean,
    onIncrease: () -> Unit = {},
    onDecrease: () -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ThemedArrowIcon(Icons.Default.KeyboardArrowUp, visible = selectable, onIncrease)

        Box(
            modifier = Modifier
                .padding(vertical = SizeS)
                .widthIn(min = 90.dp)
                .border(1.dp, colorScheme.tertiary, shape = CircleShape)
                ,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value.toString().padStart(2, '0'),
                color = colorScheme.onPrimary,
                style = typography.headlineLarge,
                modifier = Modifier.padding(horizontal = SizeM, vertical = SizeS)
            )
        }
        ThemedArrowIcon(Icons.Default.KeyboardArrowDown, visible = selectable) { onDecrease() }

        Text(
            text = label,
            modifier = Modifier.padding(top = SizeS),
            color = colorScheme.onSecondary,
            style = typography.labelLarge
        )
    }
}

@Composable
private fun ThemedArrowIcon(
    imageVector: ImageVector,
    visible: Boolean,
    onIncrease: () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(durationMillis = 500)) + slideInVertically(
            animationSpec = tween(durationMillis = 1000)
        ) { it / 2 },
        exit = fadeOut(animationSpec = tween(durationMillis = 500)) + slideOutVertically(
            animationSpec = tween(durationMillis = 1000)
        ) { it / 2 }
    ) {
        IconButton(
            onClick = onIncrease,
            modifier = Modifier
                .background(colorScheme.primary, shape = RoundedCornerShape(50))
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = "Increase",
                tint = colorScheme.surface
            )
        }
    }
}

@LightDarkPreviews
@Composable
fun TimeSelectorPreview() {
    ThemedPreview {
        TimeSelector(
            value = 10,
            label = "Minutes",
            selectable = false,
            onIncrease = {},
            onDecrease = {}
        )
    }
}

@LightDarkPreviews
@Composable
fun TimeSelectorSelectablePreview() {
    ThemedPreview {
        TimeSelector(
            value = 10,
            label = "Minutes",
            selectable = true,
            onIncrease = {},
            onDecrease = {}
        )
    }
}

