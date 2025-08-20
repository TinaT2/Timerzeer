package com.tina.timerzeer.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tina.timerzeer.core.theme.SizeM
import com.tina.timerzeer.core.theme.SizeXS

@Composable
fun RoundIconOutlinedSmall(painterResource: Int, contentDescription: String,enabled: Boolean, onClick: () -> Unit) {
    Icon(
        painter = painterResource(painterResource),
        modifier = Modifier
            .clip(CircleShape)
            .border(1.dp, shape = CircleShape, color = colorScheme.primary)
            .padding(SizeXS)
            .clickable(enabled = enabled,onClick = onClick),
        tint = colorScheme.primary,
        contentDescription = contentDescription
    )
}

@Composable
fun RoundIconFilledMedium(
    painterResource: Int,
    contentDescription: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Icon(
        painter = painterResource(painterResource),
        modifier = Modifier
            .clip(CircleShape)
            .background(shape = CircleShape, color = colorScheme.primary)
            .padding(SizeM)
            .clickable(enabled = enabled, onClick = onClick),
        tint = colorScheme.surface,
        contentDescription = contentDescription
    )
}