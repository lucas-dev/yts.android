package com.alvefard.yify.presentation.screens.details

import com.alvefard.yify.domain.model.*
import com.alvefard.yify.presentation.common.base.BaseContract
import io.reactivex.Observable

interface DetailsContract {
    interface View : BaseContract.View {

        fun getMovie(): Movie

        fun showYifyLoading()

        fun showYifyContent(detail: Detail)

        fun fillGalleryItems(items: MutableList<String>)

        fun showOmdbLoading()

        fun showOmdbContent(omdb: Omdb)

        fun observeYifyLink(): Observable<*>

        fun observeImdbLink(): Observable<*>

        fun observeBtnTrailer(): Observable<*>

        fun observeBtnFav(): Observable<*>

        fun observeBackButton(): Observable<*>

        fun observeGalleryButton(): Observable<*>

        fun observeShareButton(): Observable<*>

        fun observeHomeButton(): Observable<*>

        fun observeImageClick(): Observable<*>

        fun observeCastItemClick(): Observable<Cast>

        fun observeTorrentItemClick(): Observable<Torrent>

        fun observeGalleryItemClick(): Observable<String>

        fun showSuggestionsLoading()

        fun showSuggestions(movies: MutableList<Movie>)

        fun showCastLoading()

        fun showCastContent(castList: MutableList<Cast>)

        fun observeMovieSuggestionClick(): Observable<Movie>

        fun setFavIconOn()

        fun setFavIconOff()

        fun expandGallery()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun start()
        fun checkIsFav()
    }

}