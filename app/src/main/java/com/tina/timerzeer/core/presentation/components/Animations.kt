package com.tina.timerzeer.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable

@Composable
fun SmoothSwitchTabFadeAnimatedVisibility(visible: Boolean, content: @Composable () -> Unit) =
    AnimatedVisibility(
        visible,
        enter = fadeIn(
            animationSpec = tween(durationMillis = 500),
            initialAlpha = 0.3f
        ),
        exit = fadeOut(
            animationSpec = tween(durationMillis = 500),
            targetAlpha = 1f
        )
    ) {
        content.invoke()
    }

@Composable
fun SmoothFieldFadeAnimatedVisibility(visible: Boolean, content: @Composable () -> Unit) =
    AnimatedVisibility(
        visible,
        enter = fadeIn(
            animationSpec = tween(durationMillis = 500)
        ),
        exit = fadeOut(
            animationSpec = tween(durationMillis = 500)
        )
    ) {
        content.invoke()
    }