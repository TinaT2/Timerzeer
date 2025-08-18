package com.tina.timerzeer.app

import com.tina.timerzeer.core.domain.TimerMode
import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object TimerGraph : Route

    @Serializable
    data object Timer : Route

    @Serializable
    data class TimerFullScreen(val mode: TimerMode, val title: String, val time: Long? = null) :
        Route
}