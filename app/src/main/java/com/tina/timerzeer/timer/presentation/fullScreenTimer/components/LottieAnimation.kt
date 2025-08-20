package com.tina.timerzeer.timer.presentation.fullScreenTimer.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LottieLoader(modifier: Modifier = Modifier, resId: Int) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))

    LottieAnimation(
        modifier = modifier,
        composition = composition,
        contentScale = ContentScale.Fit
    )
}