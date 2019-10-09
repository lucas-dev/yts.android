package com.alvefard.yify.presentation.screens.moviespager

import android.view.ViewGroup
import com.alvefard.yify.presentation.screens.listing.FilterMoviesHelper
import dagger.Module
import dagger.Provides

@Module
class MoviesPagerModule(private val toolbar: ViewGroup) {

    @MoviesPagerScope
    @Provides
    internal fun provideToolbar(): ViewGroup = toolbar

    @Provides
    internal fun provideFilterMovieHelper(): FilterMoviesHelper = FilterMoviesHelper()
}
