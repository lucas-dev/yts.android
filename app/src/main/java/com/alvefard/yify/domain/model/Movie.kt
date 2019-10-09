package com.alvefard.yify.domain.model

import java.io.Serializable

data class Movie(var id: String, var title: String, var description: String, var coverMedium: String, var coverBig: String, var background: String,
                 var imdbId: String, var releaseDate: Int, var runtime: Int, var rating: Double, var ytsUrl: String,
                 var genres: MutableList<String>, var language: String, var trailer: String, var torrents: MutableList<Torrent>) : Serializable