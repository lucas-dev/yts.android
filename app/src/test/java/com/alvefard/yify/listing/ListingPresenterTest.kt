package com.alvefard.yify.listing

import com.alvefard.yify.domain.model.Movie
import com.alvefard.yify.domain.repositories.ListingRepository
import com.alvefard.yify.domain.usecases.ListingUseCase
import com.alvefard.yify.presentation.common.Constants
import com.alvefard.yify.presentation.common.ux.toastnotificator.ToastNotificator
import com.alvefard.yify.presentation.common.ux.screennavigator.ScreenNavigator
import com.alvefard.yify.presentation.screens.landing.di.rx.schedulers.TestSchedulerProvider
import com.alvefard.yify.presentation.screens.listing.FilterMoviesHelper
import com.alvefard.yify.presentation.screens.listing.ListingPresenter
import com.alvefard.yify.presentation.screens.listing.ListingScreen
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class ListingPresenterTest {
    private lateinit var subject: ListingPresenter
    private val view: ListingScreen = mock()
    private val repository: ListingRepository = mock()
    private val screenNavigator: ScreenNavigator = mock()
    private val useCase: ListingUseCase = ListingUseCase(repository)
    private val toastHelper: ToastNotificator = mock()
    private val filterMoviesHelper: FilterMoviesHelper = mock()
    private val testScheduler = TestScheduler()
    private var testSchedulerProvider = TestSchedulerProvider(testScheduler)

    @Before
    fun setUp() {
        subject = ListingPresenter(useCase, testSchedulerProvider, filterMoviesHelper,
                screenNavigator, toastHelper)
        subject.attachView(view)

        whenever(filterMoviesHelper.genre).thenReturn("")
        whenever(filterMoviesHelper.orderBy).thenReturn("")
        whenever(filterMoviesHelper.quality).thenReturn("")
        whenever(filterMoviesHelper.rating).thenReturn("")
        whenever(useCase.getLatestMovies("", "", "", "")).thenReturn(Single.just(mutableListOf()))
        whenever(useCase.getPopularMovies("", "", "", "")).thenReturn(Single.just(mutableListOf()))
        whenever(useCase.getTopRatedMovies("", "", "", "")).thenReturn(Single.just(mutableListOf()))
        whenever(view.getScreenCode()).thenReturn("")
        whenever(view.observeToolbarClick()).thenReturn(Observable.just(Unit))
        whenever(view.observeClearFilterButton()).thenReturn(Observable.just(Unit))
        whenever(view.observeFilterButton()).thenReturn(Observable.just(Unit))
        whenever(view.observeRetryLoadingButtonClick()).thenReturn(Observable.just(Unit))
        whenever(view.observeMovieClick()).thenReturn(Observable.just(Mockito.mock(Movie::class.java)))
        whenever(view.listRequestNewPage()).thenReturn(Observable.just(false))

    }

    @Test
    fun onLoadData_givenIsFirstLoad_showLoadingScreen() {
        subject.start()
        testScheduler.triggerActions()
        verify(view, times(4)).showLoadingScreen()
    }

    @Test
    fun onLoadData_givenIsNotFirstLoad_showLoadingMoreIndicator() {
        whenever(view.listRequestNewPage()).thenReturn(Observable.just(false))
        subject.start()
        testScheduler.triggerActions()

        verify(view).showLoadingMoreIndicator()
    }

    @Test
    fun onLoadMovies_givenSuccessCase_updateData() {
        whenever(useCase.getLatestMovies("", "", "", "")).thenReturn(Single.just(mutableListOf()))
        whenever(useCase.getPopularMovies("", "", "", "")).thenReturn(Single.just(mutableListOf()))
        whenever(useCase.getTopRatedMovies("", "", "", "")).thenReturn(Single.just(mutableListOf()))

        subject.start()
        testScheduler.triggerActions()

        verify(view, times(5)).updateData(any())
    }

    @Test
    fun onLoadMovies_givenErrorCase_displayError() {
        whenever(useCase.getLatestMovies("", "", "", "")).thenReturn(Single.error(Throwable("")))
        whenever(useCase.getPopularMovies("", "", "", "")).thenReturn(Single.error(Throwable("")))
        whenever(useCase.getTopRatedMovies("", "", "", "")).thenReturn(Single.error(Throwable("")))

        subject.start()
        testScheduler.triggerActions()

        verify(view, times(4)).showErrorScreen(any())
        verify(view, times(1)).showLoadingMoreErrorIndicator(any())
    }

    @Test
    fun listingScreen_givenToolbarIconClicked_expandFilters() {
        whenever(view.observeToolbarClick()).thenReturn(Observable.just(Unit))
        subject.start()
        testScheduler.triggerActions()

        verify(view, times(2)).loadFilters(any(), any(), any(), any())
        verify(view).expandBSFilter()
    }

    @Test
    fun bottomSheetFilter_givenClearFiltersBtnClicked_setUpViewsAndLoadData() {
        whenever(view.observeClearFilterButton()).thenReturn(Observable.just(Unit))
        subject.start()
        testScheduler.triggerActions()

        verify(view).resetFilters()
        verify(filterMoviesHelper).clearFilters()
        verify(view, times(2)).loadFilters(any(), any(), any(), any())
        verify(view).showFilterIconNormalState()
        verify(view).collapseBSFilterTotal()
        verify(view, times(5)).updateData(any())
    }

    @Test
    fun onFilterButtonClicked_givenFiltersChanged_CollapseBsAndSetFilterIconToNormalState() {
        whenever(filterMoviesHelper.hasFiltersChanged()).thenReturn(true)
        whenever(view.observeFilterButton()).thenReturn(Observable.just(Unit))

        subject.start()
        testScheduler.triggerActions()

        verify(view, times(2)).collapseBSFilterTotal()
        verify(view, times(2)).showFilterIconNormalState()
    }

    @Test
    fun onFilterButtonClicked_givenFiltersNotChanged_CollapseBsPartiallyAndSetFilterIconToWarningState() {
        whenever(filterMoviesHelper.hasFiltersChanged()).thenReturn(false)
        whenever(view.observeFilterButton()).thenReturn(Observable.just(Unit))

        subject.start()
        testScheduler.triggerActions()

        verify(view, times(1)).collapseBSFilterPartial()
        verify(view, times(1)).showFilterIconWarningState()
    }

    @Test
    fun listingScreen_givenRetryBtnClicked_loadData() {
        whenever(view.observeRetryLoadingButtonClick()).thenReturn(Observable.just(Unit))
        subject.start()
        testScheduler.triggerActions()

        verify(view, times(5)).updateData(any())
    }

    @Test
    fun listingScreen_givenNewPageRequested_loadData() {
        whenever(view.listRequestNewPage()).thenReturn(Observable.just(true))
        subject.start()
        testScheduler.triggerActions()

        verify(view, times(5)).updateData(any())
    }

    @Test
    fun listingScreen_givenMovieClicked_goToDetailsScreen() {
        val movie = Mockito.mock(Movie::class.java)
        whenever(view.observeMovieClick()).thenReturn(Observable.just(movie))

        subject.start()
        testScheduler.triggerActions()

        verify(screenNavigator).toDetailsPage(movie)
    }

}