package com.alvefard.yify.presentation.screens.landing.di

import com.alvefard.yify.presentation.di.DatabaseModule
import com.alvefard.yify.presentation.di.NetworkModule
import com.alvefard.yify.presentation.screens.moviespager.MoviesPagerComponent
import com.alvefard.yify.presentation.screens.moviespager.MoviesPagerModule
import com.alvefard.yify.presentation.screens.landing.LandingScreen
import com.alvefard.yify.presentation.screens.landing.di.rx.RxModule
import dagger.Subcomponent

@LandingScope
@Subcomponent(modules = [PresentationModule::class, NetworkModule::class, DatabaseModule::class, RxModule::class])
interface LandingComponent {
    operator fun plus(module: MoviesPagerModule): MoviesPagerComponent
    fun inject(target: LandingScreen)
}
