package com.tina.timerzeer.ui.stopwatch.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun StopwatchTabBar() {
    val selectedTab = rememberSaveable { mutableStateOf(0) }
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(30.dp))
            .background(Color(0xFFF1F6F9))
            .padding(4.dp)
    ) {
        TabOption(
            text = "Stopwatch",
            icon = Icons.Default.Star,
            selected = selectedTab.value == 0,
            onClick = { selectedTab.value = 0 }
        )
        TabOption(
            text = "Countdown",
            icon = Icons.Default.Star,
            selected = selectedTab.value == 1,
            onClick = { selectedTab.value = 1 }
        )
    }
}

@Composable
fun TabOption(text: String, icon: ImageVector, selected: Boolean, onClick: () -> Unit) {
    val background = if (selected) Color(0xFF009DFF) else Color.Transparent
    val contentColor = if (selected) Color.White else Color.Gray

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(30.dp))
            .background(background)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = contentColor)
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = text, color = contentColor)
    }
}
