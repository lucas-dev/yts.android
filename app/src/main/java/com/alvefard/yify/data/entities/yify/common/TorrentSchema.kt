package com.alvefard.yify.data.entities.yify.common

import com.google.gson.annotations.SerializedName

class TorrentSchema {
    @SerializedName("url")
    var url: String? = null
    @SerializedName("hash")
    var hash: String? = null
    @SerializedName("quality")
    var quality: String? = null
    @SerializedName("type")
    var type: String? = null
    @SerializedName("seeds")
    var seeds: Int? = null
    @SerializedName("peers")
    var peers: Int? = null
    @SerializedName("size")
    var size: String? = null
    @SerializedName("size_bytes")
    var sizeBytes: Double? = null
    @SerializedName("date_uploaded")
    var dateUploaded: String? = null
    @SerializedName("date_uploaded_unix")
    var dateUploadedUnix: Int? = null
}