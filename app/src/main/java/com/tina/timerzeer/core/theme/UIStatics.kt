package com.tina.timerzeer.core.theme

import com.tina.timerzeer.R

val endingAnimations = linkedMapOf(
    R.string.value_default to R.raw.firework,
    R.string.ending_animation_Explosives to R.raw.firework,
    R.string.ending_animation_fly_ribbons to R.raw.colorful,
)

val backgrounds = linkedMapOf(
    R.string.value_default to null,
    R.string.background_theme_dark to null,
    R.string.background_theme_galaxy to R.raw.galaxy,
    R.string.background_theme_digital to R.raw.watercolor_leaf,
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
