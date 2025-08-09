package com.tina.timerzeer.core.presentation.components


import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tina.timerzeer.core.theme.SizeL
import com.tina.timerzeer.core.theme.SizeM
import com.tina.timerzeer.core.theme.TimerzeerTheme
import com.tina.timerzeer.ui.components.LightDarkPreviews

@Composable
fun PrimaryButton(
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val colors = ButtonDefaults.buttonColors(
        containerColor = colorScheme.primary,
        contentColor = colorScheme.surface,
    )

    Button(
        onClick = onClick,
        enabled = enabled,
        colors = colors,
        shape = RoundedCornerShape(48.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = text,
            style = typography.bodyMedium,
            color = colorScheme.surface,
            modifier = Modifier.padding(horizontal = SizeL, vertical = SizeM)
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
