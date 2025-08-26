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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

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
    fontId: Int? = null,
    endingAnimationId: Int? = null,
    backgroundId: Int? = null,
    content: @Composable (() -> Unit)
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val isCustomized = backgrounds()[backgroundId] != null
    val customColors = if (isCustomized) {
        CustomColors(
            rowBackground = TextSecondaryTransparent,
            mainBackground = Color.Transparent,
            textColorEnabled = colorScheme.onPrimary,
            textColorDisabled = TextSecondary,
            border = MaterialTheme.colorScheme.surface
        )
    } else {
        CustomColors(
            rowBackground = colorScheme.tertiary,
            textColorEnabled = colorScheme.onPrimary,
            mainBackground = colorScheme.background,
            textColorDisabled = colorScheme.onSecondary,
            border = colorScheme.tertiary
        )
    }

    val customGraphicIds = CustomGraphicIds(
        backgroundId = backgroundId,
        fontId = fontId ?: DefaultLocalCustomGraphicIds.fontId,
        endingAnimationId = endingAnimationId ?: DefaultLocalCustomGraphicIds.endingAnimationId
    )

    val typoGraphy = buildTypography(fontStyles[fontId?: DefaultLocalCustomGraphicIds.fontId]?:fontManrope)

    CompositionLocalProvider(
        LocalCustomColors provides customColors,
        LocalCustomGraphicIds provides customGraphicIds
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typoGraphy,
            content = content
        )
    }

}