package com.alvefard.yify.domain.repositories

import com.alvefard.yify.domain.model.Movie
import io.reactivex.Completable
import io.reactivex.Single

interface FavsRepository {
    val favMovies: Single<MutableList<Movie>>

    fun isFavMovie(id: String): Single<Boolean>

    fun fav(movie: Movie): Completable

    fun unFav(movie: Movie): Completable
}
