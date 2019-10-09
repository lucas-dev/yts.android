package com.alvefard.yify.presentation.screens.search

import com.alvefard.yify.domain.model.Movie
import com.alvefard.yify.domain.usecases.SearchUseCase
import com.alvefard.yify.presentation.common.base.BasePresenter
import com.alvefard.yify.presentation.common.ux.screennavigator.ScreenNavigator
import com.alvefard.yify.presentation.screens.landing.di.rx.schedulers.BaseSchedulerProvider
import io.reactivex.Observable
import io.reactivex.observers.DisposableSingleObserver

class SearchPresenter(private val useCase: SearchUseCase,
                      private val schedulerProvider: BaseSchedulerProvider,
                      private val screenNavigator: ScreenNavigator) : BasePresenter<SearchContract.View>(), SearchContract.Presenter {

    private var searchTerm = ""
    private val movies: MutableList<Movie> = mutableListOf()
    private var currentPage = 0

    override fun start() {
        view?.let {view ->
            val list = listOf<Observable<*>>(view.inputSearchChanges().startWith(""),
                                             view.listRequestNewPage().startWith(false),
                                             view.observeRetryLoadingButtonClick().startWith(Unit))

            Observable.combineLatest(list) { objects -> objects[0] as String }
                    .skip(1)
                    .observeOn(schedulerProvider.ui())
                    .subscribe { searchTerm -> loadData(searchTerm) }

            view.observeMovieClick().subscribe { movie -> screenNavigator.toDetailsPage(movie) }

            view.observeMenuButton().subscribe { screenNavigator.openSideMenu() }
        }
    }


    private fun loadData(searchTerm: String) {
        view?.let { view ->
            if (searchTerm != this.searchTerm) {
                currentPage = 1
                movies.clear()
                view.showLoadingScreen()
            } else {
                currentPage++
                view.showLoadingMoreIndicator()
            }
            this.searchTerm = searchTerm
            addSubscription(useCase.getMovies(this.searchTerm, currentPage)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui()).subscribeWith(object : DisposableSingleObserver<List<Movie>>() {

                        override fun onSuccess(movies: List<Movie>) {
                            if (movies.isEmpty()) {
                                if (currentPage == 1) {
                                    view.showNoResultsScreen()
                                } else {
                                    view.hideLoadingMoreIndicator()
                                }
                                currentPage--
                            } else {
                                if (currentPage == 1) {
                                    view.showContentScreen()
                                } else {
                                    view.hideLoadingMoreIndicator()
                                }
                                this@SearchPresenter.movies.addAll(movies)
                                view.updateData(this@SearchPresenter.movies)
                            }

                        }

                        override fun onError(e: Throwable) {
                            if (currentPage == 1) {
                                e.message?.let { view.showErrorScreen(it) }
                                this@SearchPresenter.searchTerm = ""
                            } else {
                                e.message?.let { view.showLoadingMoreErrorIndicator(it) }
                                currentPage--
                            }
                        }
                    }))
        }

    }


}