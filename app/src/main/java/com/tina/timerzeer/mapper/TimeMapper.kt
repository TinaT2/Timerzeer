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

fun Long.plusHour(): Long {
    return this + 3600000
}

fun Long.minusHour(): Long {
    return (this - 3600000).coerceAtLeast(0)
}

fun Long.plusMinute(): Long {
    return this + 60000
}

fun Long.minusMinute(): Long {
    return (this - 60000).coerceAtLeast(0)
}

fun Long.plusSecond(): Long {
    return this + 1000
}

fun Long.minusSecond(): Long {
    return (this - 1000).coerceAtLeast(0)
}


