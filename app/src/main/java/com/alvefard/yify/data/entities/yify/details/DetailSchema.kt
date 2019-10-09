package com.alvefard.yify.data.entities.yify.details

import com.google.gson.annotations.SerializedName

class DetailSchema {
    @SerializedName("movie")
    var movie: MovieDetailsSchema? = null
}