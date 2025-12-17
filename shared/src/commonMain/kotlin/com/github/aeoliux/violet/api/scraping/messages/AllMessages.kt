package com.github.aeoliux.violet.api.scraping.messages

import com.fleeksoft.ksoup.Ksoup
import com.github.aeoliux.violet.api.ApiClient
import com.github.aeoliux.violet.api.localDateTimeFormat
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.datetime.LocalDateTime

data class MessageLabel(
    val url: String,
    val sender: String,
    val topic: String,
    val sentAt: LocalDateTime?,
    val hasAttachment: Boolean,
)

typealias MessagesList = LinkedHashMap<MessageCategories, List<MessageLabel>>
//fun MessagesList.init() {
//    this[MessageCategories.Received] = emptyList()
//    this[MessageCategories.Sent] = emptyList()
//    this[MessageCategories.Bin] = emptyList()
//}

suspend fun ApiClient.getMessages(): MessagesList {
    val categories = listOf(
        MessageCategories.Received,
        MessageCategories.Sent,
        MessageCategories.Bin
    )

    return categories.fold(MessagesList()) { acc, category ->
        val url = "https://synergia.librus.pl/wiadomosci/${category.categoryId}"
        val body = client.get(url).bodyAsText()
        val soup = Ksoup.parse(body)

        val labels = soup.select(
            "#formWiadomosci > div > div > table > tbody > tr > td:nth-child(2) > table:nth-child(2) > tbody > tr"
        ).fold(emptyList<MessageLabel>()) { list, label ->
            if (label.getAllElements().size > 2) {
                val hasAttachment = label.select("td:nth-child(2)").html().startsWith("<img ")
                val date = label.select("td:nth-child(5)").html()

                val senderData = label.select("td:nth-child(3) > a")
                val messageUrl = senderData.attr("href")
                val sender = senderData.html()

                val topic = label.select("td:nth-child(4) > a").html()

                val messageLabel = MessageLabel(
                    url = messageUrl,
                    sender = sender,
                    topic = topic,
                    sentAt = LocalDateTime.parse(date, localDateTimeFormat),
                    hasAttachment = hasAttachment
                )

                list.plus(messageLabel)
            } else
                list
        }

        acc[category] = labels
        acc
    }
}