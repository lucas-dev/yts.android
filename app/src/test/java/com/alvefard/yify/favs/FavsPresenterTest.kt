package com.alvefard.yify.favs

import com.alvefard.yify.domain.model.Movie
import com.alvefard.yify.domain.repositories.FavsRepository
import com.alvefard.yify.domain.usecases.FavUseCase
import com.alvefard.yify.presentation.common.ux.screennavigator.ScreenNavigator
import com.alvefard.yify.presentation.screens.favs.FavsPresenter
import com.alvefard.yify.presentation.screens.favs.FavsScreen
import com.alvefard.yify.presentation.screens.landing.di.rx.schedulers.TestSchedulerProvider
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class FavsPresenterTest {
    private lateinit var subject: FavsPresenter
    private val view: FavsScreen = mock()
    private val repository: FavsRepository = mock()
    private val screenNavigator: ScreenNavigator = mock()
    private val useCase: FavUseCase = FavUseCase(repository)
    private val testScheduler = TestScheduler()
    private var testSchedulerProvider = TestSchedulerProvider(testScheduler)


    @Before
    fun setUp() {
        subject = FavsPresenter(useCase, testSchedulerProvider, screenNavigator)
        subject.attachView(view)

        whenever(useCase.getFavMovies()).thenReturn(Single.just(mutableListOf()))
        whenever(view.observeMovieClick()).thenReturn(Observable.just(Mockito.mock(Movie::class.java)))
        whenever(view.observeBtnMenu()).thenReturn(Observable.just(Unit))
    }

    @Test
    fun onFavMoviesRequested_givenNoItemsFound_showEmptyView() {
        // GIVEN
        whenever(useCase.getFavMovies()).thenReturn(Single.just(mutableListOf()))
        // WHEN
        subject.start()
        testScheduler.triggerActions()
        // THEN
        verify(view).showEmptyView()
    }

    @Test
    fun onFavMoviesRequested_givenItemsFound_showContentViewAndUpdateData() {
        // GIVEN
        whenever(useCase.getFavMovies()).thenReturn(Single.just(mutableListOf(mock())))
        // WHEN
        subject.start()
        testScheduler.triggerActions()
        // THEN
        verify(view).showContentView()
        verify(view).updateData(any())
    }

    @Test
    fun favMovies_givenMovieClicked_goToDetailsPage() {
        // GIVEN
        val movie = Mockito.mock(Movie::class.java)
        whenever(view.observeMovieClick()).thenReturn(Observable.just(movie))
        // WHEN
        subject.start()
        testScheduler.triggerActions()
        // THEN
        verify(screenNavigator).toDetailsPage(movie)
    }

    @Test
    fun favMovies_givenMenuBtnClicked_openSideMenu() {
        // GIVEN
        whenever(view.observeBtnMenu()).thenReturn(Observable.just(Unit))
        // WHEN
        subject.start()
        testScheduler.triggerActions()
        // THEN
        verify(screenNavigator).openSideMenu()
    }

}