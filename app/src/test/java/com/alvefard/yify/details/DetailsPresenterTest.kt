package com.alvefard.yify.details

import com.alvefard.yify.domain.model.*
import com.alvefard.yify.domain.repositories.DetailsRepository
import com.alvefard.yify.domain.usecases.DetailsUseCase
import com.alvefard.yify.presentation.common.ux.imagepreviewer.ImagePreviewer
import com.alvefard.yify.presentation.common.ux.screennavigator.ScreenNavigator
import com.alvefard.yify.presentation.common.ux.sharemanager.ShareManager
import com.alvefard.yify.presentation.common.ux.toastnotificator.ToastNotificator
import com.alvefard.yify.presentation.common.ux.torrentmanager.TorrentManager
import com.alvefard.yify.presentation.common.ux.videopreviewer.VideoPreviewer
import com.alvefard.yify.presentation.common.ux.webviewloader.WebViewLoader
import com.alvefard.yify.presentation.screens.details.DetailsPresenter
import com.alvefard.yify.presentation.screens.details.DetailsScreen
import com.alvefard.yify.presentation.screens.landing.di.rx.schedulers.TestSchedulerProvider
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class DetailsPresenterTest {
    private lateinit var subject: DetailsPresenter
    private val view: DetailsScreen = mock()
    private val repository: DetailsRepository = mock()
    private val useCase: DetailsUseCase = DetailsUseCase(repository)
    private val chromeTabLoader: WebViewLoader = mock()
    private val videoPreviewer: VideoPreviewer = mock()
    private val screenNavigator: ScreenNavigator = mock()
    private val imagePreviewer: ImagePreviewer = mock()
    private val shareHelper: ShareManager = mock()
    private val torrentOpener: TorrentManager = mock()
    private val toastHelper: ToastNotificator = mock()
    private val testScheduler = TestScheduler()
    private var testSchedulerProvider = TestSchedulerProvider(testScheduler)

    @Before
    fun setUp() {
        subject = DetailsPresenter(useCase, testSchedulerProvider, chromeTabLoader,
                videoPreviewer, screenNavigator, imagePreviewer, shareHelper, torrentOpener,
                toastHelper)
        subject.attachView(view)

        val torrents: MutableList<Torrent> = mutableListOf()
        torrents.add(Torrent("",
                "", "", "",
                0, 0, "",
                ""))

        val movie = Movie("12", "", "",
                "", "", "", "33",
                0, 0, 0.0, "www",
                mutableListOf(), "", "", torrents)

        whenever(view.getMovie()).thenReturn(movie)
        whenever(useCase.fav(movie)).thenReturn(Completable.complete())
        whenever(useCase.getMovie("12")).thenReturn(Single.just(Mockito.mock(Detail::class.java)))
        whenever(useCase.getOmdbDetails("33")).thenReturn(Single.just(Mockito.mock(Omdb::class.java)))
        whenever(useCase.getSuggestions("12")).thenReturn(Single.just(mutableListOf()))
        whenever(useCase.isFavMovie("12")).thenReturn(Single.just(false))
        whenever(useCase.unFav(movie)).thenReturn(Completable.complete())

        whenever(view.observeYifyLink()).thenReturn(Observable.just(Unit))
        whenever(view.observeImdbLink()).thenReturn(Observable.just(Unit))
        whenever(view.observeBtnTrailer()).thenReturn(Observable.just(Unit))
        whenever(view.observeImageClick()).thenReturn(Observable.just(Unit))
        whenever(view.observeCastItemClick()).thenReturn(Observable.just(Mockito.mock(Cast::class.java)))
        whenever(view.observeTorrentItemClick()).thenReturn(Observable.just(Mockito.mock(Torrent::class.java)))
        whenever(view.observeMovieSuggestionClick()).thenReturn(Observable.just(Mockito.mock(Movie::class.java)))
        whenever(view.observeBackButton()).thenReturn(Observable.just(Unit))
        whenever(view.observeHomeButton()).thenReturn(Observable.just(Unit))
        whenever(view.observeGalleryButton()).thenReturn(Observable.just(Unit))
        whenever(view.observeShareButton()).thenReturn(Observable.just(Unit))
        whenever(view.observeGalleryItemClick()).thenReturn(Observable.just(String()))
        whenever(view.observeBtnFav()).thenReturn(Observable.just(Unit))

    }

    @Test
    fun onRequestMovieDetails_givenResultOK_loadViews() {
        whenever(useCase.getMovie("12")).thenReturn(Single.just(Mockito.mock(Detail::class.java)))
        subject.start()
        testScheduler.triggerActions()

        verify(view).showYifyContent(any())
        verify(view).showCastContent(any())
        verify(view).fillGalleryItems(any())
    }

    @Test
    fun onRequestMovieDetails_givenResultError_displayErrorMessage() {
        whenever(useCase.getMovie("12")).thenReturn(Single.error(Throwable("")))
        subject.start()
        testScheduler.triggerActions()

        verify(toastHelper, times(2)).showToast(any())
    }

    @Test
    fun onRequestOmdbDetails_givenResultOK_loadOmdbView() {
        // GIVEN
        whenever(useCase.getOmdbDetails("33")).thenReturn(Single.just(Mockito.mock(Omdb::class.java)))
        // WHEN
        subject.start()
        testScheduler.triggerActions()
        // THEN
        verify(view).showOmdbContent(any())
    }

    @Test
    fun onRequestOmdbDetails_givenResultError_displayErrorMessage() {
        // GIVEN
        whenever(useCase.getOmdbDetails("33")).thenReturn(Single.error(Throwable("")))
        // WHEN
        subject.start()
        testScheduler.triggerActions()
        // THEN
        verify(toastHelper, times(2)).showToast(any())
    }

    @Test
    fun onRequestSuggestedMovies_givenResultOK_loadSuggestionsView() {
        // GIVEN
        whenever(useCase.getSuggestions("12")).thenReturn(Single.just(mutableListOf()))
        // WHEN
        subject.start()
        testScheduler.triggerActions()
        // THEN
        verify(view, times(1)).showSuggestions(any())
    }

    @Test
    fun onRequestSuggestedMovies_givenResultError_displayErrorMessage() {
        // GIVEN
        whenever(useCase.getSuggestions("12")).thenReturn(Single.error(Throwable("")))
        // WHEN
        subject.start()
        testScheduler.triggerActions()
        // THEN
        verify(toastHelper, times(2)).showToast(any())
    }

    @Test
    fun detailScreen_givenYifyLinkClicked_launchUrl() {
        // GIVEN
        whenever(view.observeYifyLink()).thenReturn(Observable.just(Unit))
        // WHEN
        subject.start()
        testScheduler.triggerActions()
        // THEN
        verify(chromeTabLoader, times(3)).launchUrl(any())
    }

    @Test
    fun detailScreen_givenImdbLinkClicked_launchUrl() {
        // GIVEN
        whenever(view.observeImdbLink()).thenReturn(Observable.just(Unit))
        // WHEN
        subject.start()
        testScheduler.triggerActions()
        // THEN
        verify(chromeTabLoader, times(3)).launchUrl(any())
    }

    @Test
    fun detailScreen_givenTrailerLinkClicked_launchViewPreviewer() {
        // GIVEN
        whenever(view.observeBtnTrailer()).thenReturn(Observable.just(Unit))
        // WHEN
        subject.start()
        testScheduler.triggerActions()
        // THEN
        verify(videoPreviewer, times(1)).watchYoutubeVideo(any())
    }

    @Test
    fun detailScreen_givenCoverImageClicked_openImageViewerAtPosition() {
        // GIVEN
        whenever(view.observeImageClick()).thenReturn(Observable.just(Unit))
        // WHEN
        subject.start()
        testScheduler.triggerActions()
        // THEN
        verify(imagePreviewer, times(2)).openImageAt(any(), any())
    }

    @Test
    fun detailScreen_givenCastItemClicked_launchUrl() {
        // GIVEN
        whenever(view.observeCastItemClick()).thenReturn(Observable.just(Mockito.mock(Cast::class.java)))
        // WHEN
        subject.start()
        testScheduler.triggerActions()
        // THEN
        verify(chromeTabLoader, times(3)).launchUrl(any())
    }

    @Test
    fun detailScreen_givenTorrentItemClicked_openTorrentFile() {
        // GIVEN
        whenever(view.observeTorrentItemClick()).thenReturn(Observable.just(Torrent("a",
                "", "", "",
                0, 0, "",
                "")))
        // WHEN
        subject.start()
        testScheduler.triggerActions()
        // THEN
        verify(torrentOpener, times(1)).openTorrentFile("a")
    }

    @Test
    fun detailScreen_givenSuggestionItemClicked_goToDetailsScreen() {
        // GIVEN
        whenever(view.observeMovieSuggestionClick()).thenReturn(Observable.just(Mockito.mock(Movie::class.java)))
        // WHEN
        subject.start()
        testScheduler.triggerActions()
        // THEN
        verify(screenNavigator, times(1)).toDetailsPage(any())
    }

    @Test
    fun detailScreen_givenBackBtnClicked_goBack() {
        // GIVEN
        whenever(view.observeBackButton()).thenReturn(Observable.just(Unit))
        // WHEN
        subject.start()
        testScheduler.triggerActions()
        // THEN
        verify(screenNavigator, times(1)).goBack()
    }

    @Test
    fun detailScreen_givenHomeBtnClicked_popToRoot() {
        // GIVEN
        whenever(view.observeHomeButton()).thenReturn(Observable.just(Unit))
        // WHEN
        subject.start()
        testScheduler.triggerActions()
        // THEN
        verify(screenNavigator, times(1)).popToRoot()
    }

    @Test
    fun detailScreen_givenGalleryBtnClicked_expandGallery() {
        // GIVEN
        whenever(view.observeGalleryButton()).thenReturn(Observable.just(Unit))
        // WHEN
        subject.start()
        testScheduler.triggerActions()
        // THEN
        verify(view, times(1)).expandGallery()
    }

    @Test
    fun detailScreen_givenShareBtnClicked_shareMovie() {
        // GIVEN
        whenever(view.observeShareButton()).thenReturn(Observable.just(Unit))
        // WHEN
        subject.start()
        testScheduler.triggerActions()
        // THEN
        verify(shareHelper, times(1)).shareText(any(), any())
    }

    @Test
    fun detailScreen_givenGalleryItemClicked_openImageViewerAt() {
        // GIVEN
        whenever(view.observeGalleryItemClick()).thenReturn(Observable.just(String()))
        // WHEN
        subject.start()
        testScheduler.triggerActions()
        // THEN
        verify(imagePreviewer, times(2)).openImageAt(any(), any())
    }

    @Test
    fun onFavBtnClicked_givenMovieIsAlreadyFaved_unfavIt() {
        // GIVEN
        whenever(view.observeBtnFav()).thenReturn(Observable.just(Unit))
        whenever(useCase.isFavMovie("12")).thenReturn(Single.just(true))
        // WHEN
        subject.start()
        testScheduler.triggerActions()
        // THEN
        verify(view, times(1)).setFavIconOff()
        verify(toastHelper).showToast(any())
    }

    @Test
    fun onFavBtnClicked_givenMovieIsNotFaved_favIt() {
        // GIVEN
        whenever(view.observeBtnFav()).thenReturn(Observable.just(Unit))
        whenever(useCase.isFavMovie("12")).thenReturn(Single.just(false))
        // WHEN
        subject.start()
        testScheduler.triggerActions()
        // THEN
        verify(view, times(1)).setFavIconOn()
        verify(toastHelper).showToast(any())
    }

    @Test
    fun onCheckMovieIsFaved_givenMovieIsFaved_displayState() {
        // GIVEN
        whenever(useCase.isFavMovie("12")).thenReturn(Single.just(true))
        // WHEN
        subject.checkIsFav()
        testScheduler.triggerActions()
        // THEN
        verify(view).setFavIconOn()
    }

    @Test
    fun onCheckMovieIsFaved_givenMovieIsNotFaved_displayState() {
        // GIVEN
        whenever(useCase.isFavMovie("12")).thenReturn(Single.just(false))
        // WHEN
        subject.checkIsFav()
        testScheduler.triggerActions()
        // THEN
        verify(view).setFavIconOff()
    }

}