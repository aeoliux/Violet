package com.github.aeoliux.repositories

import com.github.aeoliux.api.scraping.messages.MessageCategories
import com.github.aeoliux.api.scraping.messages.getMessage
import com.github.aeoliux.api.scraping.messages.getMessages
import com.github.aeoliux.storage.AppDatabase
import com.github.aeoliux.storage.Message
import com.github.aeoliux.storage.MessageLabel
import kotlinx.coroutines.flow.map

class MessagesRepository(
    private val appDatabase: AppDatabase,
    private val clientManager: ClientManager
) {
    fun getLabelsFlow(query: String? = null) = (query
        ?.let { query ->
            this.appDatabase
                .getMessageLabelsDao()
                .getMessageLabelsByQuery(query)
        }
        ?: this.appDatabase
            .getMessageLabelsDao()
            .getMessageLabels())
        .map { labels ->
            MessageCategories
                .entries
                .associateWithTo(linkedMapOf()) { category ->
                    labels.filter { it.category == category }
                }
        }

    fun getMessageFlow(url: String) = this.appDatabase
        .getMessagesDao()
        .selectMessage(url)
        .map {
            it
                ?: this.clientManager.with { client ->
                    val message = client.getMessage(url).let {
                        Message(
                            key = this.extractKey(url) ?: return@with null,
                            url = url,
                            content = it.content,
                            attachments = listOf() // TODO!
                        )
                    }

                    this.appDatabase
                        .getMessagesDao()
                        .upsert(message)

                    message
                }
        }

    suspend fun refresh() = this.clientManager.with { client ->
        val labels = client
            .getMessages()
            .entries
            .flatMap { (category, labels) ->
                labels
                    .map { label ->
                        val key = this.extractKey(label.url) ?: return@map null

                        MessageLabel(
                            key = key,
                            category = category,
                            url = label.url,
                            sender = label.sender,
                            topic = label.topic,
                            sentAt = label.sentAt,
                            hasAttachment = label.hasAttachment
                        )
                    }
                    .filter { it != null }
                    .map { it!! }
            }

        this.appDatabase
            .getMessageLabelsDao()
            .upsertMultiple(labels)
    }

    internal fun extractKey(url: String): Int? {
        val split = url.split("/")

        return split
            .getOrNull(split.lastIndex - 1)
            ?.toInt()
    }
}