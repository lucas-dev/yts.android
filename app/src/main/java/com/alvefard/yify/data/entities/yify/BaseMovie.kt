package com.alvefard.yify.data.entities.yify

import com.alvefard.yify.data.entities.yify.common.TorrentSchema
import com.google.gson.annotations.SerializedName

open class BaseMovie {
    @SerializedName("id")
    var id: String? = null
    @SerializedName("url")
    var url: String? = null
    @SerializedName("imdb_code")
    var imdbCode: String? = null
    @SerializedName("title")
    var title: String? = null
    @SerializedName("title_english")
    var titleEnglish: String? = null
    @SerializedName("title_long")
    var titleLong: String? = null
    @SerializedName("slug")
    var slug: String? = null
    @SerializedName("year")
    var year: Int? = null
    @SerializedName("rating")
    var rating: Double? = null
    @SerializedName("runtime")
    var runtime: Int? = null
    @SerializedName("genres")
    var genres: MutableList<String>? = null
    @SerializedName("torrents")
    var torrents: MutableList<TorrentSchema>? = null
    @SerializedName("date_uploaded")
    var dateUploaded: String? = null
    @SerializedName("date_uploaded_unix")
    var dateUploadedUnix: Int? = null
    @SerializedName("description_full")
    var descriptionFull: String? = null
    @SerializedName("yt_trailer_code")
    var ytTrailerCode: String? = null
    @SerializedName("language")
    var language: String? = null
    @SerializedName("mpa_rating")
    var mpaRating: String? = null
    @SerializedName("background_image")
    var backgroundImage: String? = null
    @SerializedName("background_image_original")
    var backgroundImageOriginal: String? = null
    @SerializedName("small_cover_image")
    var smallCoverImage: String? = null
    @SerializedName("medium_cover_image")
    var mediumCoverImage: String? = null
    @SerializedName("large_cover_image")
    var largeCoverImage: String? = null
}