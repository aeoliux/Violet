package com.github.aeoliux.violet.api.scraping.messages

import kotlinx.datetime.LocalDateTime

data class Message(
    val sender: String?,
    val date: LocalDateTime,
    val topic: String,
    val content: String,
    val attachments: List<String>
)
