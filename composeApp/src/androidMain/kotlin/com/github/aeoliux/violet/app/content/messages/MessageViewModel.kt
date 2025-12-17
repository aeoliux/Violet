package com.github.aeoliux.violet.app.content.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.aeoliux.violet.repositories.MessagesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class MessageViewModel(
    private val messagesRepository: MessagesRepository
): ViewModel() {
    private var _metadata = MutableStateFlow<MessagesViewModel.MessageMetadata?>(null)
    val metadata get() = _metadata.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val message = this._metadata
        .flatMapLatest { metadata ->
            metadata
                ?.let { this.messagesRepository.getMessageFlow(it.messageLabel.url) }
                ?: emptyFlow()
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun setMetadata(metadata: MessagesViewModel.MessageMetadata) = this._metadata.update { metadata }
}