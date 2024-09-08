package com.github.aeoliux.violet

fun showAlert(title: String, e: Exception) {
    AppContext.alertTitle.value = title
    AppContext.alertMessage.value = e.message?: e.toString()
    AppContext.showAlert.value = true
}