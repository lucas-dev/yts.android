package com.alvefard.yify.presentation.common.ux.imageloader

import android.widget.ImageView

interface ImageLoader {
    fun loadImage(uri: String, target: ImageView)
    fun loadImageWithCallback(uri: String, target: ImageView, callback: ImageLoaderCallback)
}