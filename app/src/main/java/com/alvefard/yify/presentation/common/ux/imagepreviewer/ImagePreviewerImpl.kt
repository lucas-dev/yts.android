package com.alvefard.yify.presentation.common.ux.imagepreviewer

import android.app.Activity
import com.alvefard.yify.presentation.common.ux.imageloader.ImageLoader
import com.stfalcon.imageviewer.StfalconImageViewer

class ImagePreviewerImpl(internal var activity: Activity, internal var imageLoader: ImageLoader) : ImagePreviewer {

    override fun openImageAt(items: MutableList<String>, item: String) {
        StfalconImageViewer.Builder<String>(activity, items) { view, image ->
            imageLoader.loadImage(image, view)
        }.withStartPosition(items.indexOf(item)).show()
    }
}
