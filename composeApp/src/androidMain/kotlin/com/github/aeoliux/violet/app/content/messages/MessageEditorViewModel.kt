package com.github.aeoliux.violet.app.content.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.aeoliux.violet.api.types.User
import com.github.aeoliux.violet.repositories.MessagesRepository
import com.github.aeoliux.violet.storage.Message
import com.github.aeoliux.violet.storage.MessageLabel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MessageEditorViewModel(
    private val messagesRepository: MessagesRepository
): ViewModel() {
    private var _topic = MutableStateFlow("")
    val topic get() = _topic.asStateFlow()

    private var _content = MutableStateFlow("")
    val content get() = _content.asStateFlow()

    private var _requestKey = MutableStateFlow("")
    val requestKey get() = _requestKey.asStateFlow()

    private var _respondsTo = MutableStateFlow<Int?>(null)
    val respondsTo get() = _respondsTo.asStateFlow()

    private var _users = MutableStateFlow<LinkedHashMap<Int, User>>(linkedMapOf())
    val users get() = _users.asStateFlow()

    private var _selectedUsers = MutableStateFlow<List<User>>(emptyList())
    val selectedUsers get() = _selectedUsers.asStateFlow()

    fun send(onFinish: () -> Unit) {
        viewModelScope.launch {
            if (_selectedUsers.value.isEmpty())
                return@launch

            messagesRepository.sendMessage(
                topic = topic.value,
                content = content.value,
                users = selectedUsers.value,
                key = requestKey.value,
                respondsTo = respondsTo.value?.toString()
            )

            onFinish()
        }
    }

    internal suspend fun initializeSender(respondsTo: String? = null, name: String? = null) {
        val key = messagesRepository.initializeSender(respondsTo)
        val users = messagesRepository.requestUsers()

        val nameSplit = name?.split(" ")
        if (respondsTo != null && nameSplit != null && nameSplit.size >= 2) {
            val firstName = nameSplit[1]
            val lastName = nameSplit[0]

            users.entries
                .firstOrNull { (_, user) -> user.firstName == firstName && user.lastName == lastName }
                ?.let { user ->
                    _selectedUsers.update { listOf(user.value) }
                }
        }

        _requestKey.update { key }
        _users.update { users }
    }

    fun setTemplate(message: Message, messageLabel: MessageLabel) = viewModelScope.launch {
        _topic.update { "Re: ${messageLabel.topic}" }
        _content.update { """


-----
Użytkownik: ${messageLabel.sender} ${messageLabel.sentAt ?: ""} napisał:
${message.content.replace("<br>", "")}
""".trimIndent() }
        _respondsTo.update { message.key }

        initializeSender(message.key.toString(), messageLabel.sender)
    }

    fun nonResponding() = viewModelScope.launch {
        _respondsTo.update { null }
        _topic.update { "" }
        _content.update { "" }
        _selectedUsers.update { listOf() }

        initializeSender()
    }

    fun selectUsers(users: List<User>) = this._selectedUsers.update { users }

    fun setTopic(topic: String) = this._topic.update { topic }
    fun setContent(content: String) = this._content.update { content }
}