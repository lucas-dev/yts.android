package com.alvefard.yify.presentation.screens.favs

import com.alvefard.yify.data.db.MovieDatabase
import com.alvefard.yify.data.entities.MovieMapper
import com.alvefard.yify.data.repository.FavsRepositoryImpl
import com.alvefard.yify.domain.repositories.FavsRepository
import com.alvefard.yify.domain.usecases.FavUseCase
import com.alvefard.yify.presentation.common.ux.screennavigator.ScreenNavigatorConductor
import com.alvefard.yify.presentation.screens.landing.di.rx.schedulers.BaseSchedulerProvider
import dagger.Module
import dagger.Provides

@Module
class FavsModule {
    @Provides
    fun providesPresenter(useCase: FavUseCase,
                          schedulerProvider: BaseSchedulerProvider,
                          screenNavigator: ScreenNavigatorConductor): FavsContract.Presenter = FavsPresenter(useCase, schedulerProvider, screenNavigator)

    @Provides
    fun provideFavUseCase(repository: FavsRepository): FavUseCase = FavUseCase(repository)

    @Provides
    fun provideFavsRepository(movieDatabase: MovieDatabase): FavsRepository = FavsRepositoryImpl(movieDatabase, MovieMapper())
}
