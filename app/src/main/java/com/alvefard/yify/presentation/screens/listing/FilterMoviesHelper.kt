package com.alvefard.yify.presentation.screens.listing

class FilterMoviesHelper {

    lateinit var quality: String
        private set
    lateinit var genre: String
        private set
    lateinit var rating: String
        private set
    lateinit var orderBy: String
        private set

    init {
        clearFilters()
    }

    fun hasFiltersChanged(): Boolean {
        return (this.genre == DEFAULT_GENRE && this.quality == DEFAULT_QUALITY
                && this.rating == DEFAULT_RATING && this.orderBy == DEFAULT_ORDER_BY)
    }

    fun clearFilters() {
        this.quality = DEFAULT_QUALITY
        this.genre = DEFAULT_GENRE
        this.rating = DEFAULT_RATING
        this.orderBy = DEFAULT_ORDER_BY
    }

    fun updateFilters(quality: String, genre: String, rating: String, orderBy: String) {
        this.quality = quality
        this.genre = genre
        this.rating = rating
        this.orderBy = orderBy
    }

    companion object {
        const val DEFAULT_GENRE = "all"
        const val DEFAULT_QUALITY = "all"
        const val DEFAULT_RATING = "0"
        const val DEFAULT_ORDER_BY = "desc"
    }
}
