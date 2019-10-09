package com.alvefard.yify.presentation.screens.landing.di

import android.app.Activity
import android.content.Context
import androidx.drawerlayout.widget.DrawerLayout
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.alvefard.yify.presentation.common.ux.imageloader.ImageLoader
import com.alvefard.yify.presentation.common.ux.screennavigator.ScreenNavigatorConductor
import com.alvefard.yify.presentation.common.ux.screennavigator.ScreenNavigatorImpl
import com.alvefard.yify.presentation.common.ux.imageloader.ImageLoaderImpl
import com.alvefard.yify.presentation.common.ux.imagepreviewer.ImagePreviewer
import com.alvefard.yify.presentation.common.ux.imagepreviewer.ImagePreviewerImpl
import com.alvefard.yify.presentation.common.ux.sharemanager.ShareManager
import com.alvefard.yify.presentation.common.ux.sharemanager.ShareManagerImpl
import com.alvefard.yify.presentation.common.ux.toastnotificator.ToastNotificator
import com.alvefard.yify.presentation.common.ux.toastnotificator.ToastNotificatorImpl
import com.alvefard.yify.presentation.common.ux.torrentmanager.TorrentManager
import com.alvefard.yify.presentation.common.ux.torrentmanager.TorrentManagerImpl
import com.alvefard.yify.presentation.common.ux.videopreviewer.VideoPreviewer
import com.alvefard.yify.presentation.common.ux.videopreviewer.VideoPreviewerImpl
import com.alvefard.yify.presentation.common.ux.webviewloader.WebViewLoader
import com.alvefard.yify.presentation.common.ux.webviewloader.WebViewLoaderImpl
import com.alvefard.yifymovies.R
import com.bluelinelabs.conductor.Router
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import dagger.Module
import dagger.Provides

@Module
class PresentationModule(private val activity: Activity, private val router: Router, private val navDrawer: DrawerLayout) {

    @LandingScope
    @Provides
    fun provideActiviy(): Activity = activity

    @LandingScope
    @Provides
    fun provideRouter(): Router = router

    @LandingScope
    @Provides
    fun provideNavDrawer(): DrawerLayout = navDrawer

    @LandingScope
    @Provides
    fun provideScreenNavigator(router: Router, navDrawer: DrawerLayout): ScreenNavigatorConductor = ScreenNavigatorImpl(router, navDrawer)

    @LandingScope
    @Provides
    fun providesCircularProgressDrawable(context: Context): CircularProgressDrawable {
        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        return circularProgressDrawable
    }

    @LandingScope
    @Provides
    fun providesGlideRequestOptions(circularProgressDrawable: CircularProgressDrawable): RequestOptions {
        return RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .error(R.drawable.ic_no_image)
                .format(DecodeFormat.PREFER_RGB_565)
                .centerInside()
                .placeholder(circularProgressDrawable)
                .priority(Priority.HIGH)
    }

    @LandingScope
    @Provides
    fun provideImageLoader(requestOptions: RequestOptions, requestManager: RequestManager): ImageLoader = ImageLoaderImpl(requestOptions, requestManager)

    @LandingScope
    @Provides
    fun provideRequestManager(context: Context): RequestManager = Glide.with(context)

    @LandingScope
    @Provides
    fun provideImageGalleryPreviewer(activity: Activity, imageLoader: ImageLoader) : ImagePreviewer = ImagePreviewerImpl(activity, imageLoader)

    @LandingScope
    @Provides
    internal fun provideChromeCustomTabHelper(activity: Activity): WebViewLoader = WebViewLoaderImpl(activity)

    @LandingScope
    @Provides
    internal fun provideVideoPreviewerHelper(context: Activity): VideoPreviewer = VideoPreviewerImpl(context)

    @LandingScope
    @Provides
    internal fun providesToastHelper(activity: Activity): ToastNotificator = ToastNotificatorImpl(activity)

    @LandingScope
    @Provides
    internal fun provideShareClass(activity: Activity): ShareManager = ShareManagerImpl(activity)

    @LandingScope
    @Provides
    internal fun provideTorrentOpener(activity: Activity): TorrentManager = TorrentManagerImpl(activity)

}