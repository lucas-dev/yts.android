package com.alvefard.yify.data.entities.yify.movies

import com.google.gson.annotations.SerializedName

class MovieListSchema {
    @SerializedName("movie_count")
    var movieCount: Int? = null
    @SerializedName("limit")
    var limit: Int? = null
    @SerializedName("page_number")
    var pageNumber: Int? = null
    @SerializedName("movies")
    var movies: MutableList<MovieItemSchema>? = null
}