package com.tina.timerzeer.core.presentation.theme.backgrounds

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tina.timerzeer.R

val Digital: @Composable () -> Unit = {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val blurRadius = 4.dp


    Box(
        Modifier
            .fillMaxSize()
            .background(color = Color(0xFF000F15))
            .blur(blurRadius)
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(width = 303.dp, height = 329.dp)
                .offset(x = screenWidth - 380.dp, y = (-30).dp)
                .blur(blurRadius),
            painter = painterResource(R.drawable.union),
            contentDescription = stringResource(R.string.background_theme_digital)
        )

        Image(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(width = 360.dp, height = 390.dp)
                .offset(x = (-5).dp, y = (screenHeight - 800.dp))
                .blur(blurRadius)
                .scale(scaleY = -1f, scaleX = -1f),
            painter = painterResource(R.drawable.union),
            contentDescription = stringResource(R.string.background_theme_digital)
        )
    }
}

@Preview
@Composable
fun DigitalPreview() {
    Digital()
}