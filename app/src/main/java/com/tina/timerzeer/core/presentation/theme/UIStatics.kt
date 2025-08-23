package com.tina.timerzeer.core.presentation.theme

import androidx.compose.runtime.Composable
import com.tina.timerzeer.R
import com.tina.timerzeer.core.presentation.theme.backgrounds.Digital
import com.tina.timerzeer.core.presentation.theme.backgrounds.Galaxy

val endingAnimations = linkedMapOf(
    R.string.value_default to R.raw.firework,
    R.string.ending_animation_Explosives to R.raw.firework,
    R.string.ending_animation_fly_ribbons to R.raw.colorful,
)

@Composable
fun backgrounds() = linkedMapOf(
    R.string.value_default to null,
    R.string.background_theme_dark to null,
    R.string.background_theme_galaxy to Galaxy,
    R.string.background_theme_digital to Digital,
)

val backgroundToIsDark = linkedMapOf<Int, Boolean>(
    R.string.value_default to false,
    R.string.background_theme_dark to true,
    R.string.background_theme_galaxy to true,
    R.string.background_theme_digital to false
)

val fontStyles = linkedMapOf(
    R.string.value_default to R.raw.firework,
    R.string.ending_animation_Explosives to R.raw.firework,
    R.string.ending_animation_fly_ribbons to R.raw.colorful,
)
