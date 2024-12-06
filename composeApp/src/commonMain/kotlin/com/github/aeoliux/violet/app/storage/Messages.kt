package com.github.aeoliux.violet.app.storage

import com.github.aeoliux.violet.api.scraping.messages.MessageCategories
import com.github.aeoliux.violet.api.scraping.messages.MessageLabel
import com.github.aeoliux.violet.api.scraping.messages.MessagesList
import kotlinx.datetime.LocalDateTime

fun Database.selectMessageIds(): MessagesList? {
    try {
        val result = dbQuery.selectMessagesIds().executeAsList()

        return result.fold(MessagesList()) { acc, msgId ->
            val msgLabel = MessageLabel(
                url = msgId.url,
                sender = msgId.sender,
                sentAt = LocalDateTime.parse(msgId.sentAt),
                topic = msgId.topic,
                hasAttachment = msgId.hasAttachment,
            )

            val category: MessageCategories = MessageCategories.fromInt(msgId.category.toInt())
            acc[category] = acc[category]?.plus(msgLabel)?: listOf(msgLabel)

            acc
        }
    } catch (_: NullPointerException) {
        return null
    }
}

fun Database.insertMessageIds(ids: MessagesList) {
    dbQuery.transaction {
        dbQuery.clearMessagesIds()

        ids.forEach { (category, ids) ->
            ids.forEach { id ->
                dbQuery.insertMessagesIds(
                    category = category.categoryId.toLong(),
                    url = id.url,
                    sender = id.sender,
                    topic = id.topic,
                    sentAt = id.sentAt.toString(),
                    hasAttachment = id.hasAttachment
                )
            }
        }
    }
}