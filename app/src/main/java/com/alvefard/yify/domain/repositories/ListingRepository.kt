package com.alvefard.yify.domain.repositories

import com.alvefard.yify.domain.model.Movie
import io.reactivex.Single

interface ListingRepository {
    fun getPopularMovies(quality: String, genre: String, rating: String, orderBy: String): Single<MutableList<Movie>>

    fun getLatestMovies(quality: String, genre: String, rating: String, orderBy: String): Single<MutableList<Movie>>

    fun getTopRatedMovies(quality: String, genre: String, rating: String, orderBy: String): Single<MutableList<Movie>>
}
