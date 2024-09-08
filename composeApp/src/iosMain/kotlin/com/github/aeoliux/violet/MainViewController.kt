package com.github.aeoliux.violet

import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController { App(Keychain()) }