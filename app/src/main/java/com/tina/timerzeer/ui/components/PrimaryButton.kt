package com.tina.timerzeer.ui.components


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tina.timerzeer.ui.theme.TimerzeerTheme

@Composable
fun PrimaryButton(
    enabled: Boolean = true,
    outlined: Boolean = false,
    onClick: () -> Unit,
    content: @Composable ()->Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    val colors = ButtonDefaults.buttonColors(
        containerColor = if (enabled) {
            if (outlined) Color.Transparent else colorScheme.primary
        } else colorScheme.tertiary,
        contentColor = if (enabled) {
            if (outlined) colorScheme.primary else colorScheme.onPrimary
        } else colorScheme.onSecondary
    )

    val border = if (outlined) BorderStroke(1.dp, colorScheme.primary) else null

    Button(
        onClick = onClick,
        enabled = enabled,
        colors = colors,
        border = border,
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
       content()
    }
}

@Preview
@Composable
fun PrimaryButtonPreview() {
    TimerzeerTheme {
        PrimaryButton(content = { Text("Button") }, onClick =  { })
    }
}
