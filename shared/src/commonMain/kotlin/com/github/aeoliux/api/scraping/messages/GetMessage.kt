package com.github.aeoliux.api.scraping.messages

import com.fleeksoft.ksoup.Ksoup
import com.github.aeoliux.api.ApiClient
import com.github.aeoliux.api.localDateTimeFormat
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.datetime.LocalDateTime


suspend fun ApiClient.getMessage(url: String): Message {
    val url = "https://synergia.librus.pl$url"
    val soup = Ksoup.parse(
        client.get(url).bodyAsText()
    )

    val messageData = soup.select("#formWiadomosci > div > div > table > tbody > tr > td:nth-child(2)")
    val meta = messageData.select("table:nth-child(2) > tbody > tr > td:nth-child(2)")
    val content = messageData.select("div").html()

    val attachments: List<Pair<String, String>> =
        messageData.select("table:nth-child(4) > tbody > tr").foldIndexed(emptyList()) { i, acc, elem ->
            if (i == 0)
                acc
            else
                acc.plus(
                    Pair(
                        elem
                            .select("td:nth-child(1)")
                            .text(),
                        elem
                            .select("td:nth-child(2) > a > img")
                            .attr("onclick")
                            .split("\"")[1]
                            .replace("\\", "")
                    )
                )
        }

    println(attachments)

    var topic = ""
    var date = ""
    var sender: String? = null
    if (meta.size == 2) {
        topic = meta[0].html()
        date = meta[1].html()
    } else {
        sender = meta[0].html()
        topic = meta[1].html()
        date = meta[2].html()
    }

    return Message(
        sender = sender,
        date = LocalDateTime.parse(date, localDateTimeFormat),
        topic = topic,
        content = content,
        attachments = attachments
    )
}