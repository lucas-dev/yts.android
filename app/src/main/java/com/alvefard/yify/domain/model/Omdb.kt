package com.alvefard.yify.domain.model

data class Omdb(var released: String, var country: String, var awards: String, var rated: String, val imdbRating: String, val rtRating: String,
           var imdbId: String, val language: String)
