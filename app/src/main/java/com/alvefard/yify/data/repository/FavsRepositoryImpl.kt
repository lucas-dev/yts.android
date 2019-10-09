package com.alvefard.yify.data.repository

import com.alvefard.yify.data.db.MovieDatabase
import com.alvefard.yify.data.entities.db.FavData
import com.alvefard.yify.data.entities.MovieMapper
import com.alvefard.yify.domain.model.Movie
import com.alvefard.yify.domain.repositories.FavsRepository
import io.reactivex.Completable
import io.reactivex.Single

class FavsRepositoryImpl(private val database: MovieDatabase,
                         private val mapper: MovieMapper) : FavsRepository {
    override val favMovies: Single<MutableList<Movie>> = database.favsDao().fetchFavMovies().map { favData -> mapper.transformMoviesFromCache(favData) }

    override fun isFavMovie(movieId: String): Single<Boolean> = database.favsDao()
            .fetchFavMovie(movieId).onErrorReturnItem(FavData("", null))
            .map {favData -> favData.id.isNotEmpty()}

    override fun fav(movie: Movie): Completable {
        val favData = FavData(movie.id, mapper.transformMovieToSchema(movie))
        return Completable.fromAction { database.favsDao().saveMovieToFav(favData) }
    }

    override fun unFav(movie: Movie): Completable {
        val favData = FavData(movie.id, mapper.transformMovieToSchema(movie))
        return Completable.fromAction { database.favsDao().unfavMovie(favData) }
    }
}