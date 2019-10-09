package com.alvefard.yify.data.repository

import com.alvefard.yify.data.api.OmdbApi
import com.alvefard.yify.data.api.YifyApi
import com.alvefard.yify.data.entities.DetailsMapper
import com.alvefard.yify.data.entities.MovieMapper
import com.alvefard.yify.data.entities.yify.details.MovieDetailsSchema
import com.alvefard.yify.data.utils.mapNetworkErrors
import com.alvefard.yify.data.utils.retryIfNeeded
import com.alvefard.yify.domain.model.Detail
import com.alvefard.yify.domain.model.Movie
import com.alvefard.yify.domain.model.Omdb
import com.alvefard.yify.domain.repositories.DetailsRepository
import com.alvefard.yify.domain.repositories.FavsRepository
import io.reactivex.Completable
import io.reactivex.Single

class DetailsRepositoryImpl(private val yifyApiClient: YifyApi,
                            private val omdbApiClient: OmdbApi,
                            private val favsRepository: FavsRepository,
                            private val detailsMapper: DetailsMapper,
                            private val movieMapper: MovieMapper) : DetailsRepository {
    override fun getMovie(movieId: String): Single<Detail> = yifyApiClient.getMovieDetails(movieId)
            .mapNetworkErrors()
            .retryIfNeeded()
            .map { movieDetailsSchema ->
                detailsMapper.transformDetails(movieDetailsSchema.data?.movie
                        ?: MovieDetailsSchema())
            }

    override fun getOmdbDetails(movieId: String): Single<Omdb> = omdbApiClient.getOmdbDetails(movieId)
            .mapNetworkErrors()
            .retryIfNeeded()
            .map { omdbSchema -> detailsMapper.transformOmdbDetails(omdbSchema) }

    override fun getSuggestions(movieId: String): Single<MutableList<Movie>> = yifyApiClient.getMovieSuggestions(movieId)
            .mapNetworkErrors()
            .retryIfNeeded()
            .map { movieListSchemaBaseResponse -> movieMapper.transformMoviesFromSchema(movieListSchemaBaseResponse.data?.movies) }

    override fun isFavMovie(id: String): Single<Boolean> = favsRepository.isFavMovie(id)

    override fun fav(movie: Movie): Completable = favsRepository.fav(movie)

    override fun unFav(movie: Movie): Completable = favsRepository.unFav(movie)
}