package com.alvefard.yify.presentation.screens.listing

import com.alvefard.yify.domain.model.Movie
import com.alvefard.yify.domain.usecases.ListingUseCase
import com.alvefard.yify.presentation.common.Constants
import com.alvefard.yify.presentation.common.base.BasePresenter
import com.alvefard.yify.presentation.common.ux.toastnotificator.ToastNotificator
import com.alvefard.yify.presentation.common.ux.screennavigator.ScreenNavigator
import com.alvefard.yify.presentation.screens.landing.di.rx.schedulers.BaseSchedulerProvider
import io.reactivex.Single
import io.reactivex.observers.DisposableSingleObserver

class ListingPresenter(private val useCase: ListingUseCase,
                       private val schedulerProvider: BaseSchedulerProvider,
                       private val filterMoviesHelper: FilterMoviesHelper,
                       private val screenNavigator: ScreenNavigator,
                       private val toastHelper: ToastNotificator) : BasePresenter<ListingContract.View>(), ListingContract.Presenter {

    private val moviesFromModel: Single<MutableList<Movie>>
        get() {
            return when (view!!.getScreenCode()) {
                Constants.MovieType.LATEST -> useCase.getLatestMovies(filterMoviesHelper.quality, filterMoviesHelper.genre, filterMoviesHelper.rating, filterMoviesHelper.orderBy)
                Constants.MovieType.POPULAR -> useCase.getPopularMovies(filterMoviesHelper.quality, filterMoviesHelper.genre, filterMoviesHelper.rating, filterMoviesHelper.orderBy)
                Constants.MovieType.TOP_RATED -> useCase.getTopRatedMovies(filterMoviesHelper.quality, filterMoviesHelper.genre, filterMoviesHelper.rating, filterMoviesHelper.orderBy)
                else -> useCase.getTopRatedMovies(filterMoviesHelper.quality, filterMoviesHelper.genre, filterMoviesHelper.rating, filterMoviesHelper.orderBy)
            }
        }

    override fun start() {
        loadData(true)
        view?.let { view ->
            view.observeToolbarClick()
                    .subscribe {
                        view.loadFilters(filterMoviesHelper.quality, filterMoviesHelper.genre, filterMoviesHelper.rating, filterMoviesHelper.orderBy)
                        view.expandBSFilter()
                    }

            view.observeClearFilterButton()
                    .subscribe {
                        view.resetFilters()
                        filterMoviesHelper.clearFilters()
                        view.loadFilters(filterMoviesHelper.quality, filterMoviesHelper.genre, filterMoviesHelper.rating, filterMoviesHelper.orderBy)
                        view.showFilterIconNormalState()
                        view.collapseBSFilterTotal()
                        loadData(true)
                    }

            view.observeFilterButton()
                    .subscribe {
                        filterMoviesHelper.updateFilters(view.getQualityFilterVal(),
                                view.getGenreFilterVal(),
                                view.getRatingFilterVal(),
                                view.orderByFilterVal())
                        if (filterMoviesHelper.hasFiltersChanged()) {
                            view.collapseBSFilterTotal()
                            view.showFilterIconNormalState()
                        } else {
                            view.collapseBSFilterPartial()
                            view.showFilterIconWarningState()
                        }
                        loadData(true)
                    }

            view.observeRetryLoadingButtonClick().subscribe { loadData(true) }

            view.listRequestNewPage().observeOn(schedulerProvider.ui())
                    .subscribe { loadData(false) }

            view.observeMovieClick().subscribe { movie -> screenNavigator.toDetailsPage(movie) }
        }
    }

    private fun loadData(isFirstLoad: Boolean) {
        view?.let { view ->
            if (isFirstLoad)
                view.showLoadingScreen()
            else
                view.showLoadingMoreIndicator()
            addSubscription(moviesFromModel
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui()).subscribeWith(object : DisposableSingleObserver<MutableList<Movie>>() {
                        override fun onSuccess(movies: MutableList<Movie>) {
                            if (isFirstLoad) {
                                view.showContentScreen()
                            } else {
                                view.hideLoadingMoreIndicator()
                            }
                            view.updateData(movies)
                        }

                        override fun onError(e: Throwable) {
                            e.message?.let {
                                toastHelper.showToast(it)
                                if (isFirstLoad)
                                    view.showErrorScreen(it)
                                else
                                    view.showLoadingMoreErrorIndicator(it)
                            }
                        }
                    }))
        }
    }

}