package com.github.aeoliux.violet.api.scraping.messages

import com.github.aeoliux.violet.api.ApiClient
import com.github.aeoliux.violet.api.types.User
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.HttpMethod

suspend fun ApiClient.sendMessage(
    topic: String,
    content: String,
    users: List<User>,
    key: String,
    respondsTo: String?
) {
    val form = formData {
        append("requestkey", key)
        append("filtrUzytkownikow", "0")
        append("idPojemnika", "")
        append("adresat", "nauczyciel")

        respondsTo?.let {
            append("DoKogo", users[0].senderId.toString())
            append("Wid", it)
            append("idWiadomosciOrg", it)
            append("idOdpowiadajacego", "0")
            append("typ", "odp")
        } ?: users.forEach {
            append("DoKogo[]", it.senderId.toString())
            append("DoKogo_hid[]", it.senderId.toString())
        }

        if (respondsTo == null)
            append("Rodzaj", "0")

        append("temat", topic)
        append("tresc", content)

        append("poprzednia", "5")
        append("fileStorageIdentifier", "")
        append("wyslij", "Wyślij")
    }

    client.submitFormWithBinaryData(
        url = if (respondsTo == null)
            "https://synergia.librus.pl/wiadomosci/5"
        else
            "https://synergia.librus.pl/wiadomosci",
        formData = form
    ) {
        method = HttpMethod.Post
    }
}