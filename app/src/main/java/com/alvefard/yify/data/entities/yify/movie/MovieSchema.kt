package com.alvefard.yify.data.entities.yify.movie

import com.alvefard.yify.data.entities.yify.BaseMovie
import com.google.gson.annotations.SerializedName

class MovieSchema : BaseMovie() {
    @SerializedName("download_count")
    var downloadCount: Int? = null
    @SerializedName("like_count")
    var likeCount: Int? = null
    @SerializedName("description_intro")
    var descriptionIntro: String? = null

}