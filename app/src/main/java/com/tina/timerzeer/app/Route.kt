package com.tina.timerzeer.app

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object TimerGraph : Route

    @Serializable
    data object TimerPreview : Route

    @Serializable
    data object TimerFullScreen : Route
}