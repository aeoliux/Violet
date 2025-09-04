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

    fun saveFile(filename: String, content: ByteArray) {
        fileContent = content
        activity.createDocument.launch(filename)
    }
}

lateinit var fileContent: ByteArray

fun saveTo(uri: Uri, context: Context) {
    val outputStream = context.contentResolver.openOutputStream(uri)
    outputStream?.use {
        it.write(fileContent)
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun WebView(
    url: String,
    domains: List<String>,
    capture: String?,
    saveTo: String?,
    onFinish: () -> Unit,
    modifier: Modifier,
) {
    val context = koinInject<Context>()
    val apiClient = koinInject<ApiClient>()
    val keychain = koinInject<Keychain>()
    val browser = koinInject<BrowserHandler>()

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
                                val conn = URL(url).openConnection() as HttpsURLConnection
                                val cookieManager = CookieManager.getInstance()
                                val cookie = domains.fold("") { acc, domain ->
                                    acc.plus(cookieManager.getCookie(domain))
                                }
                                println(cookie)

                                conn.setRequestProperty("Cookie", cookie)
                                conn.connect()

                                val contentTypeHeader = conn.contentType ?: "application/octet-stream"
                                val encoding = conn.contentEncoding ?: "utf-8"

                                val mimeType = contentTypeHeader.split(";")[0].trim()

                                var inputStream = conn.inputStream
                                browser.saveFile(saveTo?:"file", conn.inputStream.readBytes())
                                onFinish()

                                return WebResourceResponse(
                                    mimeType,
                                    encoding,
                                    inputStream
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
    val cookieManager = android.webkit.CookieManager.getInstance()
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