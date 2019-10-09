package com.alvefard.yify.presentation.screens.details

import com.alvefard.yify.domain.model.Detail
import com.alvefard.yify.domain.model.Movie
import com.alvefard.yify.domain.model.Omdb
import com.alvefard.yify.domain.usecases.DetailsUseCase
import com.alvefard.yify.presentation.common.base.BasePresenter
import com.alvefard.yify.presentation.common.ux.imagepreviewer.ImagePreviewer
import com.alvefard.yify.presentation.common.ux.screennavigator.ScreenNavigator
import com.alvefard.yify.presentation.common.ux.sharemanager.ShareManager
import com.alvefard.yify.presentation.common.ux.toastnotificator.ToastNotificator
import com.alvefard.yify.presentation.common.ux.torrentmanager.TorrentManager
import com.alvefard.yify.presentation.common.ux.videopreviewer.VideoPreviewer
import com.alvefard.yify.presentation.common.ux.webviewloader.WebViewLoader
import com.alvefard.yify.presentation.screens.landing.di.rx.schedulers.BaseSchedulerProvider
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver

class DetailsPresenter(private val detailsUseCase: DetailsUseCase,
                       private val schedulerProvider: BaseSchedulerProvider,
                       private val chromeTabLoader: WebViewLoader,
                       private val videoPreviewer: VideoPreviewer,
                       private val screenNavigator: ScreenNavigator,
                       private val imagePreviewer: ImagePreviewer,
                       private val shareHelper: ShareManager,
                       private val torrentOpener: TorrentManager,
                       private val toastHelper: ToastNotificator) : BasePresenter<DetailsContract.View>(),
                                                                DetailsContract.Presenter {

    private var galleryItems: MutableList<String> = mutableListOf()

    override fun start() {
        requestMovieDetails()
        view?.let {view ->
            view.showOmdbLoading()
            addSubscription(detailsUseCase.getOmdbDetails(view.getMovie().imdbId)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribeWith(object : DisposableSingleObserver<Omdb>() {
                        override fun onSuccess(omdb: Omdb) {
                            view.showOmdbContent(omdb)
                        }

                        override fun onError(e: Throwable) {
                            toastHelper.showToast("Error fetching data from omdb api: " + e.message)
                        }
                    }))

            view.showSuggestionsLoading()
            addSubscription(detailsUseCase.getSuggestions(view.getMovie().id)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribeWith(object : DisposableSingleObserver<MutableList<Movie>>() {
                        override fun onSuccess(movies: MutableList<Movie>) {
                            view.showSuggestions(movies)
                        }

                        override fun onError(e: Throwable) {
                            toastHelper.showToast("Error getting data from suggestions endpoint")
                        }
                    }))

            view.observeYifyLink().subscribe { chromeTabLoader.launchUrl(view.getMovie().ytsUrl) }

            view.observeImdbLink().subscribe { chromeTabLoader.launchUrl("https://www.imdb.com/title/" + view.getMovie().imdbId) }

            view.observeBtnTrailer().subscribe { videoPreviewer.watchYoutubeVideo(view.getMovie().trailer) }

            view.observeImageClick().subscribe { imagePreviewer.openImageAt(mutableListOf(view.getMovie().coverBig), view.getMovie().coverBig) }

            view.observeCastItemClick().subscribe { o -> chromeTabLoader.launchUrl("https://www.imdb.com/name/nm"+o.imdbUrl) }

            view.observeTorrentItemClick().subscribe { o -> torrentOpener.openTorrentFile(o.url) }

            view.observeMovieSuggestionClick().subscribe { movie -> screenNavigator.toDetailsPage(movie) }

            view.observeBackButton().subscribe { screenNavigator.goBack() }

            view.observeHomeButton().subscribe { screenNavigator.popToRoot() }

            view.observeGalleryButton().subscribe { view.expandGallery() }

            view.observeShareButton().subscribe { shareHelper.shareText("Share Yify Movie", "Hey, check this movie out!. It's free for downloading and quality choises are amazing! " + view.getMovie().ytsUrl) }

            view.observeGalleryItemClick().subscribe {o -> imagePreviewer.openImageAt(galleryItems, o)}


            view.observeBtnFav().subscribe { o ->
                addSubscription(detailsUseCase.isFavMovie(view.getMovie().id)
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui()).subscribe { aBoolean ->
                            if (aBoolean!!) {
                                detailsUseCase.unFav(view.getMovie())
                                        .subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui()).subscribe {
                                            view.setFavIconOff()
                                            toastHelper.showToast("Movie unfaved")
                                        }
                            } else {
                                detailsUseCase.fav(view.getMovie())
                                        .subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui()).subscribe {
                                            view.setFavIconOn()
                                            toastHelper.showToast("Movie faved")
                                        }

                            }
                        })
            }
        }
    }

    override fun checkIsFav() {
        view?.let { view ->
            detailsUseCase.isFavMovie(view.getMovie().id).subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui()).subscribe(object : SingleObserver<Boolean> {
                        override fun onSubscribe(d: Disposable) {}

                        override fun onSuccess(aBoolean: Boolean) {
                            if (aBoolean) {
                                view.setFavIconOn()
                            } else {
                                view.setFavIconOff()
                            }
                        }

                        override fun onError(e: Throwable) {
                            toastHelper.showToast("Error reading fav data from storage: " + e.message)
                        }
                    })
        }
    }

    private fun requestMovieDetails() {
        view?.let { view ->
            view.showYifyLoading()
            view.showCastLoading()
            addSubscription(detailsUseCase.getMovie(view.getMovie().id)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribeWith(object : DisposableSingleObserver<Detail>() {
                        override fun onSuccess(detail: Detail) {
                            galleryItems = mutableListOf(detail.coverBig, detail.background, detail.screenshot1, detail.screenshot2, detail.screenshot3)
                            view.showYifyContent(detail)
                            view.showCastContent(detail.cast)
                            view.fillGalleryItems(galleryItems)
                        }

                        override fun onError(e: Throwable) {
                            toastHelper.showToast("Network call on yify api: " + e.message)
                        }
                    }))
        }
    }

}