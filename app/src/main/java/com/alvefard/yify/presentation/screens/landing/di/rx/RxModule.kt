package com.alvefard.yify.presentation.screens.landing.di.rx

import com.alvefard.yify.presentation.common.rx.RxBus
import com.alvefard.yify.presentation.screens.landing.di.LandingScope
import com.alvefard.yify.presentation.screens.landing.di.rx.schedulers.BaseSchedulerProvider
import com.alvefard.yify.presentation.screens.landing.di.rx.schedulers.SchedulerProvider
import dagger.Module
import dagger.Provides

@Module
class RxModule {

    @LandingScope
    @Provides
    fun provideRxBus(): RxBus = RxBus()

    @Provides
    @LandingScope
    fun provideSchedulers(): BaseSchedulerProvider=SchedulerProvider()
}
