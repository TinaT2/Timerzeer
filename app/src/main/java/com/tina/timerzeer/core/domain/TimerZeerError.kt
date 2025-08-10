package com.tina.timerzeer.core.domain

interface TimerZeerError {
    val message: String
}

object UnknownError: TimerZeerError{
    override val message: String
        get() = "Something went wrong"
}