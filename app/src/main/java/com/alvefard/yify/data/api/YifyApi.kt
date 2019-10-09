package com.alvefard.yify.data.api

import com.alvefard.yify.data.entities.yify.BaseResponse
import com.alvefard.yify.data.entities.yify.details.DetailSchema
import com.alvefard.yify.data.entities.yify.movies.MovieListSchema
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface YifyApi {
    @GET("list_movies.json?sort_by=download_count")
    fun getMostPopularMovies(@Query("page") page: Int,
                             @Query("quality") quality: String,
                             @Query("minimum_rating") rating: String,
                             @Query("genre") genre: String,
                             @Query("order_by") orderBy: String): Single<BaseResponse<MovieListSchema>>

    @GET("list_movies.json?sort_by=rating")
    fun getTopRatedMovies(@Query("page") page: Int,
                          @Query("quality") quality: String,
                          @Query("minimum_rating") rating: String,
                          @Query("genre") genre: String,
                          @Query("order_by") orderBy: String): Single<BaseResponse<MovieListSchema>>

    @GET("list_movies.json?sort_by=date_added")
    fun getLatestMovies(@Query("page") page: Int,
                        @Query("quality") quality: String,
                        @Query("minimum_rating") rating: String,
                        @Query("genre") genre: String,
                        @Query("order_by") orderBy: String): Single<BaseResponse<MovieListSchema>>

    @GET("list_movies.json")
    fun searchMovies(@Query("page") page: Int, @Query("query_term") queryTerm: String): Single<BaseResponse<MovieListSchema>>

    @GET("movie_details.json?with_images=true&with_cast=true")
    fun getMovieDetails(@Query("movie_id") movieId: String): Single<BaseResponse<DetailSchema>>

    @GET("movie_suggestions.json")
    fun getMovieSuggestions(@Query("movie_id") movieId: String): Single<BaseResponse<MovieListSchema>>
}