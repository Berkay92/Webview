package com.example.webview

import android.app.AlertDialog
import android.os.Bundle
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

class MainActivity : ComponentActivity() {

    companion object {
        const val websiteUrl = "www.google.com"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var webView: WebView? = null

            Column(
                modifier = Modifier.padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.image_cover),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )

                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            webViewClient = object : WebViewClient() {

                                override fun onReceivedError(
                                    view: WebView?,
                                    request: WebResourceRequest?,
                                    error: WebResourceError?
                                ) {
                                    super.onReceivedError(view, request, error)
                                    view?.loadUrl("about:blank")

                                    val alert = AlertDialog.Builder(context).create()
                                    alert.setTitle("No internet connection")
                                    alert.setIcon(android.R.drawable.ic_menu_info_details);
                                    alert.setCancelable(false);
                                    alert.setMessage("Please make sure you are connected to the internet and try again")
                                    alert.setButton(
                                        AlertDialog.BUTTON_POSITIVE,
                                        "OK"
                                    ) { dialog, _ ->
                                        dialog.dismiss()
                                    }
                                    alert.show()
                                }

                            }

                            settings.domStorageEnabled = true
                            settings.javaScriptEnabled = true
                            settings.loadWithOverviewMode = false
                            settings.allowFileAccess = true
                            settings.javaScriptCanOpenWindowsAutomatically = true
                            settings.setSupportMultipleWindows(true)
                            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

                            //webView?.clearCache(true)
                            webView = this

                            loadUrl(websiteUrl)

                        }

                    }, update = {
                        webView = it
                        it.loadUrl(websiteUrl)
                    }

                )
                BackHandler {
                    if (webView?.canGoBack() == true) {
                        webView?.goBack()
                    }
                }
            }

        }
    }
}