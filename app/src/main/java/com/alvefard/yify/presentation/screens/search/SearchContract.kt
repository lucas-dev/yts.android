package com.alvefard.yify.presentation.screens.search

import com.alvefard.yify.domain.model.Movie
import com.alvefard.yify.presentation.common.base.BaseContract
import io.reactivex.Observable

interface SearchContract {
    interface View : BaseContract.View {
        fun updateData(movies: MutableList<Movie>)
        fun inputSearchChanges(): Observable<String>
        fun listRequestNewPage(): Observable<Boolean>

        fun observeMovieClick(): Observable<Movie>

        fun observeMenuButton(): Observable<*>
        fun observeRetryLoadingButtonClick(): Observable<Unit>

        fun showLoadingScreen()
        fun showErrorScreen(msg: String)
        fun showNoResultsScreen()
        fun showContentScreen()

        fun showLoadingMoreIndicator()
        fun hideLoadingMoreIndicator()
        fun showLoadingMoreErrorIndicator(errorMsg: String)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun start()
    }

}
