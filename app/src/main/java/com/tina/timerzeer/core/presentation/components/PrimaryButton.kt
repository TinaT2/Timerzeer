package com.tina.timerzeer.core.presentation.components


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tina.timerzeer.core.theme.RoundedCornerShapeNumber
import com.tina.timerzeer.core.theme.SizeL
import com.tina.timerzeer.core.theme.SizeM
import com.tina.timerzeer.core.theme.SizeXS
import com.tina.timerzeer.core.theme.SizeXXS
import com.tina.timerzeer.core.theme.TimerzeerTheme
import com.tina.timerzeer.ui.components.LightDarkPreviews

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val colors = ButtonDefaults.buttonColors(
        containerColor = colorScheme.primary,
        contentColor = colorScheme.surface,
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
            color = colorScheme.surface,
            modifier = Modifier.padding(SizeXS)
        )
    }
}

@LightDarkPreviews
@Composable
fun PrimaryButtonPreview() {
    TimerzeerTheme {
        PrimaryButton("Button", onClick = { })
    }
}
