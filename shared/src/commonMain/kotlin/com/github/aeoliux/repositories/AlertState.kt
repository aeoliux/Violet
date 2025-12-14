package com.github.aeoliux.repositories

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AlertState {
    private val _shown = MutableStateFlow(false)
    val shown get() = _shown.asStateFlow()

    var message = ""

    fun show(message: String) {
        this.message = message
        this._shown.update { true }
    }

    fun close() = this._shown.update { false }
}