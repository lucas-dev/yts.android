package com.alvefard.yify.data.entities.yify.details

import com.alvefard.yify.data.entities.yify.BaseMovie
import com.google.gson.annotations.SerializedName


class MovieDetailsSchema : BaseMovie() {
    @SerializedName("download_count")
    var downloadCount: Int? = null
    @SerializedName("like_count")
    var likeCount: Int? = null
    @SerializedName("description_intro")
    var descriptionIntro: String? = null
    @SerializedName("cast")
    var cast: List<CastSchema>? = null
    @SerializedName("medium_screenshot_image1")
    var mediumScreenshotImage1: String? = null
    @SerializedName("medium_screenshot_image2")
    var mediumScreenshotImage2: String? = null
    @SerializedName("medium_screenshot_image3")
    var mediumScreenshotImage3: String? = null
    @SerializedName("large_screenshot_image1")
    var largeScreenshotImage1: String? = null
    @SerializedName("large_screenshot_image2")
    var largeScreenshotImage2: String? = null
    @SerializedName("large_screenshot_image3")
    var largeScreenshotImage3: String? = null
}