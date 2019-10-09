package com.alvefard.yify.search

import com.alvefard.yify.domain.model.Movie
import com.alvefard.yify.domain.repositories.SearchRepository
import com.alvefard.yify.domain.usecases.SearchUseCase
import com.alvefard.yify.presentation.common.ux.screennavigator.ScreenNavigator
import com.alvefard.yify.presentation.screens.landing.di.rx.schedulers.TestSchedulerProvider
import com.alvefard.yify.presentation.screens.search.SearchPresenter
import com.alvefard.yify.presentation.screens.search.SearchScreen
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito


class SearchPresenterTest {
    private lateinit var subject: SearchPresenter
    private val view: SearchScreen = mock()
    private val repository: SearchRepository = mock()
    private val screenNavigator: ScreenNavigator = mock()
    private val useCase: SearchUseCase = SearchUseCase(repository)
    private val testScheduler = TestScheduler()
    private var testSchedulerProvider = TestSchedulerProvider(testScheduler)

    @Before
    fun setUp() {
        subject = SearchPresenter(useCase, testSchedulerProvider, screenNavigator)
        subject.attachView(view)

        whenever(repository.getMovies(any(), any())).thenReturn(Single.just(mutableListOf()))

        whenever(view.inputSearchChanges()).thenReturn(Observable.just(""))
        whenever(view.listRequestNewPage()).thenReturn(Observable.just(false))
        whenever(view.observeRetryLoadingButtonClick()).thenReturn(Observable.just(Unit))
        whenever(view.observeMovieClick()).thenReturn(Observable.just(Mockito.mock(Movie::class.java)))
        whenever(view.observeMenuButton()).thenReturn(Observable.just(Unit))

    }

    @Test
    fun searchMovie_givenSearchTermEntered_performSearch() {
        // GIVEN
        whenever(view.inputSearchChanges()).thenReturn(Observable.just("batman"))
        // WHEN
        subject.start()
        testScheduler.triggerActions()
        // THEN
        verify(repository).getMovies("batman", 1)
    }

    @Test
    fun searchMovie_givenNewPageRequested_loadNewPage() {
        // GIVEN
        whenever(repository.getMovies(any(), any())).thenReturn(Single.just(mutableListOf(mock())))
        whenever(view.inputSearchChanges()).thenReturn(Observable.just("batman"))
        // WHEN
        subject.start()
        testScheduler.triggerActions()
        subject.start()
        testScheduler.triggerActions()
        // THEN
        verify(view).showLoadingMoreIndicator()
        verify(view).hideLoadingMoreIndicator()
        verify(repository).getMovies("batman", 2)
    }

    @Test
    fun searchMovie_givenRetryButtonPressed_loadPage() {
        // GIVEN
        whenever(view.inputSearchChanges()).thenReturn(Observable.just("batman"))
        whenever(view.observeRetryLoadingButtonClick()).thenReturn(Observable.just(Unit))
        // WHEN
        subject.start()
        testScheduler.triggerActions()
        // THEN
        verify(repository).getMovies("batman", 1)
    }

    @Test
    fun onRetryButtonPressed_givenSuccessLoadWasPage3_loadPage3() {
        // GIVEN
        whenever(repository.getMovies(any(), any())).thenReturn(Single.just(mutableListOf(mock())))
        whenever(view.inputSearchChanges()).thenReturn(Observable.just("batman"))
        // WHEN
        subject.start()
        testScheduler.triggerActions()
        subject.start()
        testScheduler.triggerActions()
        subject.start()
        testScheduler.triggerActions()
        subject.start()
        testScheduler.triggerActions()
        // THEN
        verify(repository).getMovies("batman", 3)

    }

    @Test
    fun searchMovie_givenMovieItemClick_goToDetailsScreen() {
        // GIVEN
        val movie = Mockito.mock(Movie::class.java)
        whenever(view.observeMovieClick()).thenReturn(Observable.just(movie))
        // WHEN
        subject.start()
        // THEN
        verify(screenNavigator).toDetailsPage(movie)
    }

    @Test
    fun searchMovie_givenMenuButtonClicked_openSideMenu() {
        // GIVEN
        val movie = Mockito.mock(Movie::class.java)
        whenever(view.observeMenuButton()).thenReturn(Observable.just(Unit))
        // WHEN
        subject.start()
        // THEN
        verify(screenNavigator).openSideMenu()
    }

    @Test
    fun searchMovie_givenNewSearchTermEntered_performNewSearch() {
        whenever(view.inputSearchChanges()).thenReturn(Observable.just("batman"))
        subject.start()
        subject.start()
        testScheduler.triggerActions()

        verify(repository).getMovies("batman", 2)

        whenever(view.inputSearchChanges()).thenReturn(Observable.just("superman"))
        subject.start()
        testScheduler.triggerActions()


        verify(repository).getMovies("superman", 1)
    }

    @Test
    fun onNoMoviesFound_givenIsFirstSearchWithTerm_showNoResultsScreen() {
        whenever(view.inputSearchChanges()).thenReturn(Observable.just("batman"))
        subject.start()
        testScheduler.triggerActions()

        verify(view).showNoResultsScreen()
    }

    @Test
    fun onNoMoviesFound_givenIsNotFirstSearchWithTerm_hideLoadingMoreIndicator() {
        whenever(view.inputSearchChanges()).thenReturn(Observable.just("batman"))
        subject.start()
        subject.start()
        testScheduler.triggerActions()

        whenever(view.inputSearchChanges()).thenReturn(Observable.just("superman"))
        subject.start()
        testScheduler.triggerActions()

        verify(view).hideLoadingMoreIndicator()
    }

    @Test
    fun onMoviesFound_givenIsFirstSearchWithTerm_showContentScreenAndUpdateData() {
        // GIVEN
        whenever(repository.getMovies(any(), any())).thenReturn(Single.just(mutableListOf(mock())))
        whenever(view.inputSearchChanges()).thenReturn(Observable.just("batman"))
        // WHEN
        subject.start()
        testScheduler.triggerActions()
        // THEN
        verify(view).showContentScreen()
        verify(view).updateData(any())
    }

    @Test
    fun onMoviesFound_givenIsNotFirstSearchWithTerm_hideLoadingMoreIndicatorAndUpdateData() {
        // GIVEN
        whenever(repository.getMovies(any(), any())).thenReturn(Single.just(mutableListOf(mock())))
        whenever(view.inputSearchChanges()).thenReturn(Observable.just("batman"))
        // WHEN
        subject.start()
        subject.start()
        testScheduler.triggerActions()
        // THEN
        verify(view, times(2)).hideLoadingMoreIndicator()
        verify(view, times(2)).updateData(any())
    }

    @Test
    fun onSearchError_givenIsFirstSearchWithTerm_showErrorScreen() {
        // GIVEN
        var throwable = Throwable("error msg")
        whenever(repository.getMovies(any(), any())).thenReturn(Single.error(throwable))
        whenever(view.inputSearchChanges()).thenReturn(Observable.just("batman"))
        // WHEN
        subject.start()
        testScheduler.triggerActions()
        // THEN
        verify(view).showErrorScreen("error msg")
    }

    @Test
    fun onSearchError_givenIsNotFirstSearchWithTerm_showErrorMessage() {
        whenever(repository.getMovies(any(), any())).thenReturn(Single.just(mutableListOf(mock())))
        whenever(view.inputSearchChanges()).thenReturn(Observable.just("batman"))

        subject.start()
        testScheduler.triggerActions()

        var throwable = Throwable("error msg")
        whenever(repository.getMovies(any(), any())).thenReturn(Single.error(throwable))

        subject.start()
        testScheduler.triggerActions()

        verify(view).showLoadingMoreErrorIndicator("error msg")

    }

}