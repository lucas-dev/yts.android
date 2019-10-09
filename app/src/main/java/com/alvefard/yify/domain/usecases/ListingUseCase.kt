package com.alvefard.yify.domain.usecases

import com.alvefard.yify.domain.model.Movie
import com.alvefard.yify.domain.repositories.ListingRepository
import io.reactivex.Single

class ListingUseCase(private val repository: ListingRepository) {

    fun getPopularMovies(quality: String, genre: String, rating: String, orderBy: String): Single<MutableList<Movie>> = this.repository.getPopularMovies(quality, genre, rating, orderBy)

    fun getLatestMovies(quality: String, genre: String, rating: String, orderBy: String): Single<MutableList<Movie>> = this.repository.getLatestMovies(quality, genre, rating, orderBy)

    fun getTopRatedMovies(quality: String, genre: String, rating: String, orderBy: String): Single<MutableList<Movie>> = this.repository.getTopRatedMovies(quality, genre, rating, orderBy)

}