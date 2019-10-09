package com.alvefard.yify.presentation.common.ux.videopreviewer

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

class VideoPreviewerImpl(private val context: Context): VideoPreviewer {

    override fun watchYoutubeVideo(id: String) {
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
        appIntent.putExtra("force_fullscreen", true)
        val webIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=$id"))
        try {
            context.startActivity(appIntent)
        } catch (ex: ActivityNotFoundException) {
            context.startActivity(webIntent)
        }

    }
}
