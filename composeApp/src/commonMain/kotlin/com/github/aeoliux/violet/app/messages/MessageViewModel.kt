package com.github.aeoliux.violet.app.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.aeoliux.violet.api.scraping.messages.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MessageViewModel(
    private val fetch: suspend (url: String) -> Message?
): ViewModel() {
    private var _message = MutableStateFlow<Message?>(null)
    val message get() = _message.asStateFlow()

    fun fetchMessage(url: String) {
        viewModelScope.launch {
            _message.update {
                fetch(url)
            }
        }
    }

    fun closeMessage() {
        viewModelScope.launch {
            _message.update { null }
        }
    }
}