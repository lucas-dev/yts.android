package com.alvefard.yify.presentation.screens.listing

import com.alvefard.yify.domain.model.Movie
import com.alvefard.yify.presentation.common.base.BaseContract
import io.reactivex.Observable

interface ListingContract {
    interface View : BaseContract.View {

        fun getQualityFilterVal(): String

        fun getGenreFilterVal(): String

        fun getRatingFilterVal(): String

        fun orderByFilterVal(): String

        fun getScreenCode(): String
        //    loading state
        fun showLoadingScreen()

        //    error state
        fun showErrorScreen(errorMsg: String)

        fun observeRetryLoadingButtonClick(): Observable<*>

        //    filter dialog
        fun observeClearFilterButton(): Observable<*>

        fun observeFilterButton(): Observable<*>

        fun collapseBSFilterPartial()

        fun collapseBSFilterTotal()

        fun showFilterIconNormalState()

        fun showFilterIconWarningState()

        fun loadFilters(quality: String, genre: String, rating: String, orderBy: String)

        fun expandBSFilter()

        fun resetFilters()

        //    toolbar
        fun observeToolbarClick(): Observable<*>

        //    load more
        fun showLoadingMoreIndicator()

        fun hideLoadingMoreIndicator()

        fun showLoadingMoreErrorIndicator(errorMsg: String)

        //    content state
        fun showContentScreen()

        fun listRequestNewPage(): Observable<Boolean>

        fun observeMovieClick(): Observable<Movie>

        fun updateData(movie: MutableList<Movie>)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun start()
    }

}