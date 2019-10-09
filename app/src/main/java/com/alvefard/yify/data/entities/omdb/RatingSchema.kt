package com.alvefard.yify.data.entities.omdb

import com.google.gson.annotations.SerializedName

data class RatingSchema(
        @SerializedName("Source") var source: String? = null,
        @SerializedName("Value") var value: String? = null
)