package com.tina.timerzeer.core.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily

val LightColorScheme = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    tertiary = Tertiary,
    background = BgPrimary,
    surface = OnColorWhite,
    error = Error,
    onPrimary = TextPrimary,
    onSecondary = TextSecondary,
)

val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    secondary = SecondaryDark,
    tertiary = TertiaryDark,
    background = BgPrimaryDark,
    surface = OnColorWhiteDark,
    error = ErrorDark,
    onPrimary = TextPrimaryDark,
    onSecondary = TextSecondaryDark,
)


@Composable
fun TimerzeerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    fontFamily: FontFamily = fontManrope,
    isCustomisedBackground:Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }


    val customColors = if (isCustomisedBackground) {
        CustomColors(
            rowBackground = TextSecondaryTransparent,
            textColorEnabled = colorScheme.onPrimary,
            textColorDisabled = TextSecondary
        )
    } else {
        CustomColors(
            rowBackground = colorScheme.tertiary,
            textColorEnabled = colorScheme.onPrimary,
            textColorDisabled = colorScheme.onSecondary
        )
    }


    val typoGraphy = buildTypography(fontFamily)

    CompositionLocalProvider(LocalCustomColors provides customColors){
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typoGraphy,
            content = content
        )
    }

}