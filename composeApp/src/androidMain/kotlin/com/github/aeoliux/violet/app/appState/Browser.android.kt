package com.github.aeoliux.violet.app.appState

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.github.aeoliux.violet.Keychain
import com.github.aeoliux.violet.MainActivity
import com.github.aeoliux.violet.api.ApiClient
import com.github.aeoliux.violet.app.components.LoadingIndicator
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url
import io.ktor.utils.io.core.Input
import org.koin.compose.koinInject
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

actual class BrowserHandler(private val context: Context, private val activity: MainActivity) {
    actual suspend fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    actual suspend fun saveFile(filename: String, content: ByteArray, onFinish: () -> Unit) {
        fileContent = content
        onFinishDownload = onFinish
        activity.createDocument.launch(filename)
    }
}

lateinit var fileContent: ByteArray
lateinit var onFinishDownload: () -> Unit

fun saveTo(uri: Uri, context: Context) {
    val outputStream = context.contentResolver.openOutputStream(uri)
    outputStream?.use {
        it.write(fileContent)
    }

    onFinishDownload()
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun WebView(
    url: String,
    domains: List<String>,
    capture: String?,
    onFinish: (url: String, cookies: LinkedHashMap<String, String>) -> Unit,
    modifier: Modifier,
) {
    val context = koinInject<Context>()
    val apiClient = koinInject<ApiClient>()
    val keychain = koinInject<Keychain>()

    var cookies by remember { mutableStateOf(emptyList<Cookie>()) }
    LaunchedEffect(Unit) {
        Model.logIn(apiClient, keychain)
        cookies = domains.fold(emptyList()) { acc, domain ->
            acc.plus(apiClient.cookieStorage.get(Url(domain)))
        }
    }

    if (cookies.isNotEmpty()) {
        AndroidView(factory = {
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                domains.forEach { setCookies(context, it, cookies) }

                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        return false
                    }

                    @Deprecated("Deprecated in Java")
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        url: String?
                    ): Boolean {
                        return false
                    }

                    override fun shouldInterceptRequest(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): WebResourceResponse? {
                        if (request != null && capture != null) {
                            val url = request.url.toString()
                            if (url.contains(capture)) {
                                val cookieManager = CookieManager.getInstance()
                                val cookies: LinkedHashMap<String, String> = domains.fold(LinkedHashMap()) { acc, domain ->
                                    if (!acc.containsKey(domain)) acc[domain] = ""
                                    acc[domain] = acc[domain].plus(cookieManager.getCookie(domain))
                                    acc
                                }

                                onFinish(url, cookies)

                                return WebResourceResponse(
                                    "text/plain",
                                    "utf-8",
                                    ByteArrayInputStream(
                                        ByteArray(0)
                                    )
                                )
                            }
                        }

                        return super.shouldInterceptRequest(view, request)
                    }
                }

                loadUrl(url)
            }
        }, modifier = modifier)
    } else {
        LoadingIndicator()
    }
}

fun setCookies(context: Context, url: String, cookies: List<Cookie>) {
    val cookieManager = CookieManager.getInstance()
    cookieManager.setAcceptCookie(true)
    cookieManager.setAcceptThirdPartyCookies(WebView(context), true)

    cookies.forEach {
        cookieManager.setCookie(
            url,
            "${it.name}=${it.value}; domain=${it.domain}; path=${it.path};"
        )
    }

    cookieManager.flush()
}