package com.tina.timerzeer.core.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.tina.timerzeer.R


val fontManrope = FontFamily(
    Font(R.font.manrope_regular, FontWeight.Normal),
    Font(R.font.manrope_medium, FontWeight.Medium),
    Font(R.font.manrope_semibold, FontWeight.SemiBold),
    Font(R.font.manrope_bold, FontWeight.Bold),
    Font(R.font.manrope_extrabold, FontWeight.ExtraBold),
    Font(R.font.manrope_light, FontWeight.Light),
    Font(R.font.manrope_extralight, FontWeight.ExtraLight),
)

// Set of Material typography styles to start with
val Typography = Typography(
    headlineLarge = TextStyle(
        fontSize = 42.sp,
        fontFamily = fontManrope,
        fontWeight = FontWeight(700),
    ),
    headlineMedium = TextStyle(
        fontSize = 24.sp,
        fontFamily = fontManrope,
        fontWeight = FontWeight(700),
    ),
    headlineSmall = TextStyle(
        fontSize = 20.sp,
        fontFamily = fontManrope,
        fontWeight = FontWeight(600),
    ),
    bodyLarge = TextStyle(
        fontSize = 22.sp,
        fontFamily = fontManrope,
        fontWeight = FontWeight(500),
    ),
    bodyMedium = TextStyle(
        fontSize = 16.sp,
        fontFamily = fontManrope,
        fontWeight = FontWeight(500),
    ),
    labelLarge = TextStyle(
        fontSize = 12.sp,
        fontFamily = fontManrope,
        fontWeight = FontWeight(500),
    )
)
