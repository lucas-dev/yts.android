package com.alvefard.yify.presentation.screens.favs

import com.alvefard.yify.domain.model.Movie
import com.alvefard.yify.presentation.common.base.BaseContract
import io.reactivex.Observable

interface FavsContract {
    interface View : BaseContract.View {
        fun showLoadingScreen()
        fun showContentView()
        fun showEmptyView()
        fun updateData(movies: MutableList<Movie>)
        fun observeMovieClick(): Observable<Movie>
        fun observeBtnMenu(): Observable<*>
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun start()
    }

}