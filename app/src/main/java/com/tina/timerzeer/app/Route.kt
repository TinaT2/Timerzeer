package com.tina.timerzeer.app

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object TimerGraph:Route

    @Serializable
    data object Timer : Route

    @Serializable
    data object TimerStarted : Route
}