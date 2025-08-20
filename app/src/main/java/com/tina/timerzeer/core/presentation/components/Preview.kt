package com.tina.timerzeer.core.presentation.components

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.tina.timerzeer.core.theme.TimerzeerTheme

@Composable
fun ThemedPreview(content: @Composable () -> Unit) {
    TimerzeerTheme {
        content()
    }
}

@Preview(
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFFAFCFC
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF132026
)
annotation class LightDarkPreviews
