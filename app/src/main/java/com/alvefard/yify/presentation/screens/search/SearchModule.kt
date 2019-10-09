package com.alvefard.yify.presentation.screens.search

import com.alvefard.yify.data.api.YifyApi
import com.alvefard.yify.data.entities.MovieMapper
import com.alvefard.yify.data.repository.SearchRepositoryImpl
import com.alvefard.yify.domain.repositories.SearchRepository
import com.alvefard.yify.domain.usecases.SearchUseCase
import com.alvefard.yify.presentation.common.ux.screennavigator.ScreenNavigatorConductor
import com.alvefard.yify.presentation.screens.landing.di.rx.schedulers.BaseSchedulerProvider
import dagger.Module
import dagger.Provides

@Module
class SearchModule {
    @Provides
    fun providesPresenter(useCase: SearchUseCase,
                          schedulerProvider: BaseSchedulerProvider,
                          screenNavigator: ScreenNavigatorConductor): SearchContract.Presenter =
            SearchPresenter(useCase, schedulerProvider, screenNavigator)

    @Provides
    fun providesUseCase(repository: SearchRepository): SearchUseCase = SearchUseCase(repository)

    @Provides
    fun providesModel(apiClient: YifyApi): SearchRepository = SearchRepositoryImpl(apiClient, MovieMapper())
}