package com.alvefard.yify.presentation.screens.favs

import com.alvefard.yify.domain.usecases.FavUseCase
import com.alvefard.yify.presentation.common.base.BasePresenter
import com.alvefard.yify.presentation.common.ux.screennavigator.ScreenNavigator
import com.alvefard.yify.presentation.screens.landing.di.rx.schedulers.BaseSchedulerProvider

class FavsPresenter(private val useCase: FavUseCase,
                    private val schedulerProvider: BaseSchedulerProvider,
                    private val screenNavigator: ScreenNavigator) : BasePresenter<FavsContract.View>(), FavsContract.Presenter {

    override fun start() {
        view?.let {view ->
            view.showLoadingScreen()
            addSubscription(useCase.getFavMovies().subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui()).subscribe { movies ->
                if (movies.isEmpty()) {
                    view.showEmptyView()
                } else {
                    view.showContentView()
                    view.updateData(movies)
                }
            })

            view.observeMovieClick().subscribe { movie -> screenNavigator.toDetailsPage(movie) }

            view.observeBtnMenu().subscribe { screenNavigator.openSideMenu() }
        }
    }
}