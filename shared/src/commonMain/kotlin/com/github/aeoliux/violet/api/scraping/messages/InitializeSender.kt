package com.github.aeoliux.violet.api.scraping.messages

import com.fleeksoft.ksoup.Ksoup
import com.github.aeoliux.violet.api.ApiClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

suspend fun ApiClient.initializeSender(respondsTo: String?): String {
    val body = this.client.get(
        "https://synergia.librus.pl/wiadomosci/${respondsTo?.let { "3/5/${it}" } ?: "2/5"}"
    ).bodyAsText()

    val parser = Ksoup.parse(body)
    val key = parser.select("#formWiadomosci > input:first-child")

    return key.value()
}