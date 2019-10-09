package com.alvefard.yify.data.entities

import com.alvefard.yify.data.entities.omdb.OmdbSchema
import com.alvefard.yify.data.entities.yify.details.MovieDetailsSchema
import com.alvefard.yify.domain.model.Cast
import com.alvefard.yify.domain.model.Detail
import com.alvefard.yify.domain.model.Omdb
import com.alvefard.yify.domain.model.Torrent

class DetailsMapper {
    fun transformDetails(movieSchema: MovieDetailsSchema): Detail {
        val torrents : MutableList<Torrent> = mutableListOf()
        movieSchema.torrents?.forEach {
            torrents.add(Torrent(it.url?:"",
                    it.hash?:"", it.quality?:"", it.type?:"",
                    it.seeds?:0, it.peers?:0, it.size?:"",
                    it.dateUploaded?:""))
        }

        val cast : MutableList<Cast> = mutableListOf()
        movieSchema.cast?.forEach {
            cast.add(Cast(it.name?:"", it.urlSmallImage?:"", it.characterName?:"", it.imdbCode?:""))
        }
        return Detail(movieSchema.id?:"", movieSchema.imdbCode?:"", movieSchema.url?:"", movieSchema.title?:"", movieSchema.downloadCount?:0,
                movieSchema.descriptionFull?:"", movieSchema.ytTrailerCode?:"", movieSchema.language?:"",
                movieSchema.mediumCoverImage?:"", movieSchema.largeCoverImage?:"", movieSchema.backgroundImage?:"",
                movieSchema.descriptionFull?:"",
                movieSchema.genres?: mutableListOf(),
                movieSchema.runtime?:0, torrents, cast, (movieSchema.year?:0).toString(),
                movieSchema.largeScreenshotImage1?:"", movieSchema.largeScreenshotImage2?:"", movieSchema.largeScreenshotImage3?:"")
    }

    fun transformOmdbDetails(omdbSchema: OmdbSchema): Omdb {
        val imdbRating = omdbSchema.ratings?.get(0)
        val rtRating = if (omdbSchema.ratings != null && omdbSchema.ratings!!.size > 1) omdbSchema.ratings?.get(1) else null
        return Omdb(omdbSchema.released?:"", omdbSchema.country?:"",
                omdbSchema.awards?:"", omdbSchema.rated?:"", if (imdbRating != null) imdbRating.value?:"" else "-",
                if (rtRating != null) rtRating.value?:"" else "-", omdbSchema.imdbID?:"", omdbSchema.language?:"")
    }

}