package com.github.aeoliux.violet.app.appState

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Not implemented yet
actual var runBackgroundTask: (task: suspend () -> Unit) -> Unit = { task ->
    CoroutineScope(Dispatchers.Default).launch { task() }
}