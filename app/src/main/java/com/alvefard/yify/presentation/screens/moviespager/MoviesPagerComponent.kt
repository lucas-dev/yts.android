package com.alvefard.yify.presentation.screens.moviespager

import com.alvefard.yify.presentation.screens.about.AboutScreen
import com.alvefard.yify.presentation.screens.details.DetailsModule
import com.alvefard.yify.presentation.screens.details.DetailsScreen
import com.alvefard.yify.presentation.screens.favs.FavsModule
import com.alvefard.yify.presentation.screens.favs.FavsScreen
import com.alvefard.yify.presentation.screens.listing.ListingModule
import com.alvefard.yify.presentation.screens.listing.ListingScreen
import com.alvefard.yify.presentation.screens.search.SearchModule
import com.alvefard.yify.presentation.screens.search.SearchScreen
import dagger.Subcomponent

@MoviesPagerScope
@Subcomponent(modules = [MoviesPagerModule::class, SearchModule::class, FavsModule::class, ListingModule::class, DetailsModule::class])
interface MoviesPagerComponent {
    fun inject(target: SearchScreen)

    fun inject(target: MoviesPagerScreen)

    fun inject(target: FavsScreen)

    fun inject(target: ListingScreen)

    fun inject(target: DetailsScreen)

    fun inject(target: AboutScreen)
}