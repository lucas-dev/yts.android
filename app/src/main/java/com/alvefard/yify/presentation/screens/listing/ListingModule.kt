package com.alvefard.yify.presentation.screens.listing

import com.alvefard.yify.data.api.YifyApi
import com.alvefard.yify.data.db.MovieDatabase
import com.alvefard.yify.data.entities.MovieMapper
import com.alvefard.yify.data.repository.ListingCloudDataStore
import com.alvefard.yify.data.repository.ListingLocalDataStore
import com.alvefard.yify.data.repository.ListingRepositoryImpl
import com.alvefard.yify.domain.repositories.ListingRepository
import com.alvefard.yify.domain.usecases.ListingUseCase
import com.alvefard.yify.presentation.common.ux.toastnotificator.ToastNotificator
import com.alvefard.yify.presentation.common.ux.screennavigator.ScreenNavigatorConductor
import com.alvefard.yify.presentation.screens.landing.di.rx.schedulers.BaseSchedulerProvider
import dagger.Module
import dagger.Provides

@Module
class ListingModule {

    @Provides
    fun provideListingPresenter(useCase: ListingUseCase,
                                schedulerProvider: BaseSchedulerProvider,
                                filterMoviesHelper: FilterMoviesHelper,
                                screenNavigator: ScreenNavigatorConductor,
                                toastHelper: ToastNotificator): ListingContract.Presenter = ListingPresenter(useCase, schedulerProvider, filterMoviesHelper, screenNavigator, toastHelper)

    @Provides
    fun provideUseCase(repository: ListingRepository): ListingUseCase = ListingUseCase(repository)

    @Provides
    fun provideListingRepository(apiClient: YifyApi, database: MovieDatabase): ListingRepository = ListingRepositoryImpl(ListingCloudDataStore(apiClient), ListingLocalDataStore(database), MovieMapper())

}