package com.alvefard.yify.presentation

import android.app.Activity
import android.app.Application
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import com.alvefard.yify.presentation.di.builder.AppComponent
import com.alvefard.yify.presentation.di.builder.AppModule
import com.alvefard.yify.presentation.di.builder.DaggerAppComponent
import com.alvefard.yify.presentation.screens.moviespager.MoviesPagerComponent
import com.alvefard.yify.presentation.screens.moviespager.MoviesPagerModule
import com.alvefard.yify.presentation.screens.landing.di.LandingComponent
import com.alvefard.yify.presentation.screens.landing.di.PresentationModule
import com.alvefard.yifymovies.BuildConfig
import com.bluelinelabs.conductor.Router
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber

class App : Application() {
    companion object {
        lateinit var appComponent: AppComponent
        lateinit var landingComponent: LandingComponent
        lateinit var moviesPagerComponent: MoviesPagerComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = createAppComponent()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)

    }

    private fun createAppComponent(): AppComponent {
        return DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    fun createLandingComponent(activity: Activity, router: Router, navDrawer: DrawerLayout): LandingComponent {
        landingComponent = appComponent.plus(PresentationModule(activity, router, navDrawer))
        return landingComponent
    }

    fun createMoviesPagerComponent(layout: ViewGroup): MoviesPagerComponent {
        moviesPagerComponent = landingComponent.plus(MoviesPagerModule(layout))
        return moviesPagerComponent
    }

}