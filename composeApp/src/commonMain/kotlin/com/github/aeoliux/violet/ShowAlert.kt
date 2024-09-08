package com.github.aeoliux.violet

fun showAlert(title: String, e: Exception) {
    Context.alertTitle.value = title
    Context.alertMessage.value = e.message?: e.toString()
    Context.showAlert.value = true
}