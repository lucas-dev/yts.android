package com.alvefard.yify.data.entities

import com.alvefard.yify.data.entities.db.FavData
import com.alvefard.yify.data.entities.yify.common.TorrentSchema
import com.alvefard.yify.data.entities.yify.movies.MovieItemSchema
import com.alvefard.yify.domain.model.Movie
import com.alvefard.yify.domain.model.Torrent

class MovieMapper {
    fun transformMoviesFromSchema(movieItemSchemas: MutableList<MovieItemSchema>?): MutableList<Movie> {
        val movies: MutableList<Movie> = mutableListOf()
        movieItemSchemas?.forEach {
            val torrents: MutableList<Torrent> = mutableListOf()
            it.torrents?.forEach { torrentSchema ->
                torrents.add(Torrent(torrentSchema.url ?: "",
                        torrentSchema.hash ?: "", torrentSchema.quality ?: "", torrentSchema.type
                        ?: "",
                        torrentSchema.seeds ?: 0, torrentSchema.peers ?: 0, torrentSchema.size
                        ?: "",
                        torrentSchema.dateUploaded ?: ""))
            }
            movies.add(Movie(it.id ?: "", it.title ?: "", it.descriptionFull ?: "",
                    it.mediumCoverImage ?: "", it.largeCoverImage
                    ?: "", it.backgroundImage ?: "", it.imdbCode ?: "",
                    it.year ?: 0, it.runtime ?: 0, it.rating ?: 0.0, it.url ?: "",
                    it.genres ?: mutableListOf(), it.language ?: "", it.ytTrailerCode
                    ?: "", torrents))
        }
        return movies
    }

    fun transformMoviesFromCache(favData: MutableList<FavData>): MutableList<Movie> {
        val movies: MutableList<Movie> = mutableListOf()
        for ((_, movie) in favData) {
            movie?.let {
                val torrents: MutableList<Torrent> = mutableListOf()
                for (torrent in movie.torrents ?: mutableListOf()) {
                    torrents.add(Torrent(torrent.url ?: "",
                            torrent.hash ?: "", torrent.quality ?: "", torrent.type ?: "",
                            torrent.seeds ?: 0, torrent.peers ?: 0, torrent.size ?: "",
                            torrent.dateUploaded ?: ""))
                }
                movies.add(Movie(movie.id ?: "", movie.title ?: "", movie.descriptionFull ?: "",
                        movie.mediumCoverImage?: "", movie.largeCoverImage
                        ?: "", movie.backgroundImage ?: "", movie.imdbCode ?: "",
                        movie.year ?: 0, movie.runtime ?: 0, movie.rating ?: 0.0, movie.url ?: "",
                        movie.genres ?: mutableListOf(), movie.language ?: "", movie.ytTrailerCode
                        ?: "", torrents))
            }
        }
        return movies
    }

    fun transformMovieToSchema(movie: Movie): MovieItemSchema {
        val torrents: MutableList<TorrentSchema> = mutableListOf()
        movie.torrents.forEach {
            val torrentSchema = TorrentSchema()
            torrentSchema.url = it.url
            torrentSchema.hash = it.hash
            torrentSchema.quality = it.quality
            torrentSchema.type = it.type
            torrentSchema.seeds = it.seeds
            torrentSchema.peers = it.peers
            torrentSchema.size = it.size
            torrentSchema.dateUploaded = it.dateUploaded
            torrents.add(torrentSchema)
        }

        val movieItemSchema = MovieItemSchema()
        movieItemSchema.id = movie.id
        movieItemSchema.title = movie.title
        movieItemSchema.backgroundImage = movie.background
        movieItemSchema.descriptionFull = movie.description
        movieItemSchema.mediumCoverImage = movie.coverMedium
        movieItemSchema.largeCoverImage = movie.coverBig
        movieItemSchema.imdbCode = movie.imdbId
        movieItemSchema.year = movie.releaseDate
        movieItemSchema.runtime = movie.runtime
        movieItemSchema.rating = movie.rating
        movieItemSchema.url = movie.ytsUrl
        movieItemSchema.genres = movie.genres
        movieItemSchema.language = movie.language
        movieItemSchema.ytTrailerCode = movie.trailer
        movieItemSchema.torrents = torrents

        return movieItemSchema
    }

}