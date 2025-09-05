package com.github.aeoliux.violet.app.appState

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

expect class BrowserHandler {
    suspend fun openUrl(url: String)
    suspend fun saveFile(filename: String, content: ByteArray, onFinish: () -> Unit)
}

@Composable
expect fun WebView(
    url: String,
    domains: List<String>,
    capture: String?,
    onFinish: (url: String, cookies: LinkedHashMap<String, String>) -> Unit,
    modifier: Modifier,
)