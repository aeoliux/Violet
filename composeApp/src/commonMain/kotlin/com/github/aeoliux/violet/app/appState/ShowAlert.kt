package com.github.aeoliux.violet.app.appState

fun AppState.showAlert(title: String, e: Exception) {
    alertTitle.value = title
    alertMessage.value = e.message?: e.toString()
    showAlert.value = true
}