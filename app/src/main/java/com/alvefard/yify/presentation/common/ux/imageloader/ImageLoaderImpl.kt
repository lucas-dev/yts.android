package com.alvefard.yify.presentation.common.ux.imageloader

import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

class ImageLoaderImpl(private val requestOptions: RequestOptions, private val requestManager: RequestManager):ImageLoader {

    override fun loadImage(uri: String, target: ImageView) {
        getCommonRequestBuilder(uri)
                .skipMemoryCache(true).dontTransform()
                .into(target)
    }

    override fun loadImageWithCallback(uri: String, target: ImageView, callback: ImageLoaderCallback) {
        getCommonRequestBuilder(uri)
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Bitmap>, isFirstResource: Boolean): Boolean {
                        callback.onError()
                        return false
                    }

                    override fun onResourceReady(resource: Bitmap, model: Any, target: Target<Bitmap>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        callback.onComplete()
                        return false
                    }
                })
                .into(target)
    }

    private fun getCommonRequestBuilder(uri: String): RequestBuilder<Bitmap> {
        return requestManager
                .asBitmap()
                .load(uri)
                .apply(requestOptions)
    }

}
