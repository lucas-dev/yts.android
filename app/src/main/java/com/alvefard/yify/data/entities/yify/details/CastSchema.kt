package com.alvefard.yify.data.entities.yify.details

import com.google.gson.annotations.SerializedName

class CastSchema {
    @SerializedName("name")
    var name: String? = null
    @SerializedName("character_name")
    var characterName: String? = null
    @SerializedName("url_small_image")
    var urlSmallImage: String? = null
    @SerializedName("imdb_code")
    var imdbCode: String? = null
}