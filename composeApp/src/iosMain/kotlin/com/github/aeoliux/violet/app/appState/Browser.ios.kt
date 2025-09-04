package com.github.aeoliux.violet.app.appState

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual class BrowserHandler {
    actual suspend fun openUrl(url: String) {
        val nsUrl = NSURL.URLWithString(url)
        if (nsUrl != null) {
            UIApplication.sharedApplication.openURL(nsUrl)
        }
    }
}

@Composable
actual fun WebView(
    url: String,
    domains: List<String>,
    capture: String?,
    saveTo: String?,
    onFinish: () -> Unit,
    modifier: Modifier,
) {
}