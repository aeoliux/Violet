package com.github.aeoliux.violet.app.appState

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

expect class BrowserHandler {
    suspend fun openUrl(url: String)
}

@Composable
expect fun WebView(
    url: String,
    domains: List<String>,
    capture: String?,
    saveTo: String?,
    onFinish: () -> Unit,
    modifier: Modifier,
)