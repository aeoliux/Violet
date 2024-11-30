package com.github.aeoliux.violet.app.appState

expect var runBackgroundTask: (task: suspend () -> Unit) -> Unit