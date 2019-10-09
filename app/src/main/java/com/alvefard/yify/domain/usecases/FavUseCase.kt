package com.alvefard.yify.domain.usecases

import com.alvefard.yify.domain.model.Movie
import com.alvefard.yify.domain.repositories.FavsRepository
import io.reactivex.Single

class FavUseCase(private val repository: FavsRepository) {

    fun getFavMovies(): Single<MutableList<Movie>> {
        return repository.favMovies
    }

}