package com.tina.timerzeer.mapper

data class TimeComponents(
    val hours: Int,
    val minutes: Int,
    val seconds: Int
)

fun Long.toTimeComponents(): TimeComponents {
    val totalSeconds = this / 1000
    val hours = (totalSeconds / 3600).toInt()
    val minutes = ((totalSeconds % 3600) / 60).toInt()
    val seconds = (totalSeconds % 60).toInt()
    return TimeComponents(hours, minutes, seconds)
}
