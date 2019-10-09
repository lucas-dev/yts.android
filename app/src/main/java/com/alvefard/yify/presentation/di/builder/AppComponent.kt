package com.alvefard.yify.presentation.di.builder

import com.alvefard.yify.presentation.screens.landing.di.LandingComponent
import com.alvefard.yify.presentation.screens.landing.di.PresentationModule
import dagger.Component

@AppScope
@Component(modules = [AppModule::class])
interface AppComponent {
    operator fun plus(presentationModule: PresentationModule): LandingComponent
}
