package com.tina.timerzeer.core.presentation.components


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tina.timerzeer.core.theme.RoundedCornerShapeNumber
import com.tina.timerzeer.core.theme.SizeXS
import com.tina.timerzeer.timer.presentation.components.LightDarkPreviews
import com.tina.timerzeer.timer.presentation.components.ThemedPreview

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
