package com.github.aeoliux.violet.app.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.aeoliux.violet.Keychain
import com.github.aeoliux.violet.api.ApiClient
import com.github.aeoliux.violet.api.scraping.messages.Message
import com.github.aeoliux.violet.api.scraping.messages.MessageLabel
import com.github.aeoliux.violet.api.scraping.messages.getMessage
import com.github.aeoliux.violet.app.appState.BrowserHandler
import com.github.aeoliux.violet.app.appState.Model
import com.github.aeoliux.violet.app.storage.AppDatabase
import com.github.aeoliux.violet.app.storage.MessagesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MessageViewModel(
    private val client: ApiClient,
    private val keychain: Keychain,
    private val messagesRepository: MessagesRepository,
    private val browserHandler: BrowserHandler
): ViewModel() {
    private var _message = MutableStateFlow<Message?>(null)
    val message get() = _message.asStateFlow()

    private var _download = MutableStateFlow<Pair<String, String>?>(null)
    val download get() = _download.asStateFlow()

    fun fetchMessage(label: MessageLabel) {
        viewModelScope.launch {
            _message.update {
                when (messagesRepository.doesExist(label.url)) {
                    true -> messagesRepository.getMessage(label)
                    false -> {
                        Model.logIn(client, keychain)
                        val message = client.getMessage(label.url)
                        messagesRepository.insertMessage(label.url, message)
                        message
                    }
                }
            }
        }
    }

    fun closeMessage() {
        viewModelScope.launch {
            _message.update { null }
        }
    }

    fun downloadFile(filename: String, url: String) {
        viewModelScope.launch {
            _download.update { Pair(filename, url) }
        }
    }

    fun closeDownload() {
        viewModelScope.launch {
            _download.update { null }
        }
    }
}