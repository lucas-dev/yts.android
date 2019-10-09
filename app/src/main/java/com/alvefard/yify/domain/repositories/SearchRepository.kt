package com.alvefard.yify.domain.repositories

import com.alvefard.yify.domain.model.Movie
import io.reactivex.Single

interface SearchRepository {
    fun getMovies(searchTerm: String, page: Int): Single<MutableList<Movie>>
}