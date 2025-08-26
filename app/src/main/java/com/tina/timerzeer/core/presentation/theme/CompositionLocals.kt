package com.tina.timerzeer.core.presentation.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color


@Immutable
data class CustomColors(
    val rowBackground: Color,
    val mainBackground: Color,
    val textColorEnabled: Color,
    val textColorDisabled: Color,
    val border: Color
)

val LocalCustomColors = staticCompositionLocalOf {
    CustomColors(
        rowBackground = Color.Gray,
        mainBackground = Color.Transparent,
        textColorEnabled = Color.Black,
        textColorDisabled = Color.DarkGray,
        border = Color.DarkGray,
    )
}

@Immutable
data class CustomGraphicIds(
    val backgroundId: Int?,
    val endingAnimationId :Int,
    val fontId: Int
)

val LocalCustomGraphicIds = staticCompositionLocalOf {
    DefaultLocalCustomGraphicIds
}

val DefaultLocalCustomGraphicIds =  CustomGraphicIds(
    backgroundId = null,
    endingAnimationId = endingAnimations.keys.first(),
    fontId = fontStyles.keys.first()
)