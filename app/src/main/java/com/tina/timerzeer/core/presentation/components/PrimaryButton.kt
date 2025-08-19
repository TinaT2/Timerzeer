package com.tina.timerzeer.core.presentation.components


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tina.timerzeer.R
import com.tina.timerzeer.core.theme.RoundedCornerShapeNumber
import com.tina.timerzeer.core.theme.SizeXS

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val colors = ButtonDefaults.buttonColors(
        containerColor = colorScheme.primary,
        disabledContainerColor = colorScheme.tertiary
    )

    Button(
        onClick = onClick,
        enabled = enabled,
        colors = colors,
        shape = RoundedCornerShape(RoundedCornerShapeNumber),
        modifier = modifier
            .wrapContentSize()
            .fillMaxWidth()
    ) {
        Text(
            text = text,
            style = typography.bodyMedium,
            color = if (enabled) colorScheme.surface else colorScheme.onSecondary,
            modifier = Modifier.padding(SizeXS)
        )
    }
}

@Composable
fun OutlinedPrimaryButton(
    text: String,
    leadingIcon: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val color = if (enabled) colorScheme.primary else colorScheme.tertiary
    val textColor = if (enabled) colorScheme.primary else colorScheme.onSecondary

    val bgColor = ButtonDefaults.buttonColors(
        containerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent
    )
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = bgColor,
        border = BorderStroke(1.dp, color),
        shape = RoundedCornerShape(RoundedCornerShapeNumber),
        modifier = modifier
            .wrapContentSize()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(leadingIcon), tint = textColor, contentDescription = text)
            Spacer(Modifier.width(SizeXS))
            Text(
                text = text,
                style = typography.bodyMedium,
                color = textColor,
            )
        }

    }
}

@LightDarkPreviews
@Composable
fun PrimaryButtonPreview() {
    ThemedPreview {
        PrimaryButton("Button", onClick = { })
    }
}

@LightDarkPreviews
@Composable
fun PrimaryButtonDisablePreview() {
    ThemedPreview {
        PrimaryButton("Button", enabled = false, onClick = { })
    }
}

@LightDarkPreviews
@Composable
fun OutlinedPrimaryButtonPreview() {
    ThemedPreview {
        OutlinedPrimaryButton("Button", leadingIcon = R.drawable.property_1_calendar, onClick = { })
    }
}

@LightDarkPreviews
@Composable
fun OutlinedPrimaryButtonDisablePreview() {
    ThemedPreview {
        OutlinedPrimaryButton(
            "Button",
            enabled = false,
            leadingIcon = R.drawable.property_1_calendar,
            onClick = { })
    }
}
