package com.github.aeoliux.violet.repositories

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

    suspend fun <T> task(closure: suspend () -> T): T? {
        try {
            return closure()
        } catch (e: Exception) {
            this.show(e.message ?: e.stackTraceToString())

            return null
        }
    }
}