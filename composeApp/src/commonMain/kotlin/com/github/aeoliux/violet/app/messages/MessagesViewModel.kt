package com.github.aeoliux.violet.app.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.aeoliux.violet.api.scraping.messages.MessageCategories
import com.github.aeoliux.violet.api.scraping.messages.MessageLabel
import com.github.aeoliux.violet.api.scraping.messages.MessagesList
import com.github.aeoliux.violet.app.storage.MessageLabelsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MessagesViewModel(private val repository: MessageLabelsRepository): ViewModel() {
    private var _messagesCategory = MutableStateFlow(MessageCategories.Received)
    val messagesCategory get() = _messagesCategory.asStateFlow()

    private var _messages = MutableStateFlow(MessagesList())
    val messages get() = _messages.asStateFlow()

    private var _selectedMessage = MutableStateFlow<MessageLabel?>(null)
    val selectedMessage get() = _selectedMessage.asStateFlow()

    fun selectCategory(category: MessageCategories) {
        viewModelScope.launch {
            _messagesCategory.update {
                category
            }
        }
    }

    fun selectMessages() {
        viewModelScope.launch {
            _messages.update { repository.getMessageLabels() }
        }
    }

    fun selectMessage(label: MessageLabel?) {
        viewModelScope.launch {
            _selectedMessage.update { label }
        }
    }
}