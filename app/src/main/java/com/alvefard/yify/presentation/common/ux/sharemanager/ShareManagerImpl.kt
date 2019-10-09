package com.alvefard.yify.presentation.common.ux.sharemanager

import android.app.Activity
import android.content.Intent

class ShareManagerImpl(internal var activity: Activity): ShareManager {

    override fun shareText(title: String, content: String) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.putExtra(Intent.EXTRA_TEXT, content)
        activity.startActivity(Intent.createChooser(share, title))
    }
}