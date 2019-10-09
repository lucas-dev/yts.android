package com.alvefard.yify.presentation.common.ux.screennavigator

import com.alvefard.yify.domain.model.Movie

interface ScreenNavigator {
    fun presentMainScreen()
    fun toLatestMoviesPage()
    fun toPopularMoviesPage()
    fun toTopRatedMoviesPage()
    fun toDetailsPage(movie: Movie)
    fun toSearchPage()
    fun toFavsPage()
    fun toAboutPage()
    fun goBack()
    fun popToRoot()
    fun openSideMenu()
    fun lockSideMenu()
    fun unlockSideMenu()
}