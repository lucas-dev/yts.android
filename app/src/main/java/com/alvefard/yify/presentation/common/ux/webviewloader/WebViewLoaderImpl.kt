package com.alvefard.yify.presentation.common.ux.webviewloader

import android.app.Activity
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

class WebViewLoaderImpl(private val activity: Activity):WebViewLoader {

    override fun launchUrl(url: String) {
        CustomTabsIntent.Builder().build().launchUrl(activity, Uri.parse(url))
    }

}