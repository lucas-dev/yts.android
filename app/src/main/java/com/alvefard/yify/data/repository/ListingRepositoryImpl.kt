package com.alvefard.yify.data.repository

import com.alvefard.yify.data.Constants.COLUMN_LATEST
import com.alvefard.yify.data.Constants.COLUMN_POPULAR
import com.alvefard.yify.data.Constants.COLUMN_RATED
import com.alvefard.yify.data.entities.FilterData
import com.alvefard.yify.data.entities.db.MovieListData
import com.alvefard.yify.data.entities.MovieMapper
import com.alvefard.yify.data.entities.yify.movies.MovieItemSchema
import com.alvefard.yify.domain.model.Movie
import com.alvefard.yify.domain.repositories.ListingRepository
import io.reactivex.Single

class ListingRepositoryImpl(private var cloudDatastore: ListingCloudDataStore,
                            private var localDatastore: ListingLocalDataStore,
                            private var mapper: MovieMapper) : ListingRepository {

    private fun getMovies(type: String, quality: String, genre: String, rating: String, orderBy: String): Single<MutableList<MovieItemSchema>> {
        val filters = FilterData(quality, genre, rating, orderBy)
        return localDatastore.getMoviesFromCache(type)!!.flatMap { movieListData: MovieListData ->
            val movieListData2 = localDatastore.getCachedMovie(type, filters, movieListData)
            cloudDatastore.getMoviesFromNetwork(type, movieListData2.currentPage + 1, filters)!!.map { movieListSchemaBaseResponse ->
                val movies : MutableList<MovieItemSchema> = mutableListOf()
                if (movieListData2.movies != null)
                    movies.addAll(movieListData2.movies)
                movies.addAll(movieListSchemaBaseResponse.data!!.movies!!)

                localDatastore.updateMoviesByType(type, movies, movieListSchemaBaseResponse.data!!.pageNumber!!, filters)

                movies
            }
        }
    }


    override fun getPopularMovies(quality: String, genre: String, rating: String, orderBy: String): Single<MutableList<Movie>> {
        return getMovies(COLUMN_POPULAR, quality, genre, rating, orderBy).map { movieItemSchemas -> mapper.transformMoviesFromSchema(movieItemSchemas) }
    }

    override fun getLatestMovies(quality: String, genre: String, rating: String, orderBy: String): Single<MutableList<Movie>> {
        return getMovies(COLUMN_LATEST, quality, genre, rating, orderBy).map { movieItemSchemas -> mapper.transformMoviesFromSchema(movieItemSchemas) }
    }

    override fun getTopRatedMovies(quality: String, genre: String, rating: String, orderBy: String): Single<MutableList<Movie>> {
        return getMovies(COLUMN_RATED, quality, genre, rating, orderBy).map { movieItemSchemas -> mapper.transformMoviesFromSchema(movieItemSchemas) }
    }
}