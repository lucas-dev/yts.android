package com.alvefard.yify.domain.model

import java.io.Serializable

data class Detail(val id: String, val imdb: String, var url: String, var title: String, var downloadCount: Int, var synopsis: String, var trailer: String,
                  var language: String, var coverMedium: String, var coverBig: String, val background: String, val description: String,
                  var genres: MutableList<String>, var runtime: Int,
                  var torrents: MutableList<Torrent>, var cast: MutableList<Cast>, val releaseDate: String,
                  val screenshot1: String, val screenshot2: String, val screenshot3: String) : Serializable