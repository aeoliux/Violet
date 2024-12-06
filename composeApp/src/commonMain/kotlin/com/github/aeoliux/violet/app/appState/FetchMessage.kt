package com.github.aeoliux.violet.app.appState

import com.github.aeoliux.violet.api.scraping.messages.Message
import com.github.aeoliux.violet.api.scraping.messages.getMessage

suspend fun AppState.fetchMessage(url: String): Message? {
    this.logIn()
    return try {
        this.client.value.getMessage(url)
    } catch (e: Exception) {
        this.alertTitle.value = "Fetching message error"
        this.alertMessage.value = e.toString()
        this.showAlert.value = true

        null
    }
}