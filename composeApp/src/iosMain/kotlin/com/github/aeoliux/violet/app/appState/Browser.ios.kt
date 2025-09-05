package com.github.aeoliux.violet.app.appState

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import com.github.aeoliux.violet.Keychain
import com.github.aeoliux.violet.api.ApiClient
import com.github.aeoliux.violet.app.components.LoadingIndicator
import io.ktor.http.Cookie
import io.ktor.http.Url
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCSignatureOverride
import kotlinx.cinterop.readValue
import org.koin.compose.koinInject
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSHTTPCookie
import platform.Foundation.NSHTTPCookieDomain
import platform.Foundation.NSHTTPCookieName
import platform.Foundation.NSHTTPCookiePath
import platform.Foundation.NSHTTPCookiePropertyKey
import platform.Foundation.NSHTTPCookieSecure
import platform.Foundation.NSHTTPCookieStorage
import platform.Foundation.NSHTTPCookieValue
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.Foundation.setValue
import platform.UIKit.UIApplication
import platform.WebKit.WKNavigation
import platform.WebKit.WKNavigationAction
import platform.WebKit.WKNavigationActionPolicy
import platform.WebKit.WKNavigationDelegateProtocol
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration
import platform.WebKit.javaScriptEnabled
import platform.darwin.NSObject

actual class BrowserHandler {
    actual suspend fun openUrl(url: String) {
        val nsUrl = NSURL.URLWithString(url)
        if (nsUrl != null) {
            UIApplication.sharedApplication.openURL(nsUrl)
        }
    }

    actual suspend fun saveFile(
        filename: String,
        content: ByteArray,
        onFinish: () -> Unit
    ) {
    }
}

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun WebView(
    url: String,
    domains: List<String>,
    capture: String?,
    onFinish: (url: String, cookies: LinkedHashMap<String, String>) -> Unit,
    modifier: Modifier,
) {
    val keychain = koinInject<Keychain>()
    val apiClient = koinInject<ApiClient>()
    val browserHandler = koinInject<BrowserHandler>()

    var cookies by remember { mutableStateOf(emptyList<Map<NSHTTPCookiePropertyKey?, String>>()) }

    LaunchedEffect(Unit) {
        Model.logIn(apiClient, keychain)

        cookies = domains.fold(emptyList()) { acc, domain ->
            var acc2 = acc
            apiClient.cookieStorage.get(Url(domain)).forEach {
                val path = it.path?:"/"
                acc2 = acc2.plus(
                    mapOf(
                        NSHTTPCookieName to it.name,
                        NSHTTPCookieValue to it.value,
                        NSHTTPCookieDomain to domain,
                        NSHTTPCookiePath to path,
                        NSHTTPCookieSecure to if (it.secure) "TRUE" else "FALSE"
                    )
                )
            }

            acc2
        }
    }

    if (cookies.isNotEmpty())
        UIKitView(factory = {
            WKWebView(
                frame = CGRectZero.readValue(),
                configuration = WKWebViewConfiguration().apply {
                    preferences.javaScriptEnabled = true
                }
            ).apply {
                navigationDelegate = object : NSObject(), WKNavigationDelegateProtocol {
                    @ObjCSignatureOverride
                    override fun webView(
                        webView: WKWebView,
                        decidePolicyForNavigationAction: WKNavigationAction,
                        decisionHandler: (WKNavigationActionPolicy) -> Unit
                    ) {
                        capture?.let {
                            val url =
                                decidePolicyForNavigationAction.request.URL?.absoluteString ?: ""
                            if (url.contains(it)) {
                                val cookies =
                                    domains.fold(LinkedHashMap<String, String>()) { acc, domain ->
                                        NSHTTPCookieStorage.sharedHTTPCookieStorage
                                            .cookiesForURL(NSURL(string = domain))?.forEach {
                                                it as NSHTTPCookie
                                                if (!acc.containsKey(domain)) acc[domain] = ""
                                                acc[domain] =
                                                    acc[domain].plus("${it.name}=${it.value};")
                                            }

                                        acc
                                    }

                                onFinish(url, cookies)
                                return
                            }
                        }

                        decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyAllow)
                    }
                }

                val nsCookies = cookies.map { props ->
                    NSHTTPCookie(props as Map<Any?, *>)
                }

                fun setCookies(cookies: List<NSHTTPCookie>) {
                    if (cookies.isEmpty()) {
                        loadRequest(NSURLRequest(NSURL(string = url)))
                        println("loading: $url")
                        return
                    }

                    configuration.websiteDataStore.httpCookieStore.setCookie(cookies[0]) {
                        setCookies(cookies.drop(1))
                    }
                }

                setCookies(nsCookies)
            }
        }, modifier = Modifier.fillMaxSize())
    else
        LoadingIndicator()
}

