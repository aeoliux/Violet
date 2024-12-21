package com.github.aeoliux.violet.app.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.aeoliux.violet.Keychain
import com.github.aeoliux.violet.api.ApiClient
import com.github.aeoliux.violet.api.scraping.messages.Message
import com.github.aeoliux.violet.api.scraping.messages.getMessage
import com.github.aeoliux.violet.app.appState.Model
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MessageViewModel(
    private val client: ApiClient,
    private val keychain: Keychain
): ViewModel() {
    private var _message = MutableStateFlow<Message?>(null)
    val message get() = _message.asStateFlow()

    fun fetchMessage(url: String) {
        viewModelScope.launch {
            _message.update {
                Model.logIn(client, keychain)
                client.getMessage(url)
            }
        }
    }

    fun closeMessage() {
        viewModelScope.launch {
            _message.update { null }
        }
    }
}