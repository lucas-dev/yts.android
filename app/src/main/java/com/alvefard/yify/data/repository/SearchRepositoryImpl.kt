package com.alvefard.yify.data.repository

import com.alvefard.yify.data.api.YifyApi
import com.alvefard.yify.data.entities.MovieMapper
import com.alvefard.yify.domain.model.Movie
import com.alvefard.yify.domain.repositories.SearchRepository
import io.reactivex.Single

class SearchRepositoryImpl(private val apiClient: YifyApi, private val movieMapper: MovieMapper) : SearchRepository {

    override fun getMovies(searchTerm: String, page: Int): Single<MutableList<Movie>> = apiClient.searchMovies(page, searchTerm).map {
        movieMapper.transformMoviesFromSchema(it.data!!.movies)
    }

}