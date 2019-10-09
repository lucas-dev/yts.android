package com.alvefard.yify.domain.usecases

import com.alvefard.yify.domain.model.Movie
import com.alvefard.yify.domain.repositories.SearchRepository
import io.reactivex.Single

class SearchUseCase(private var searchRepository: SearchRepository) {
    fun getMovies(searchTerm: String, page: Int): Single<MutableList<Movie>> = searchRepository.getMovies(searchTerm, page)
}