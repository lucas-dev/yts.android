package com.alvefard.yify.data.entities.yify

import com.google.gson.annotations.SerializedName

class BaseResponse<T> {
    @SerializedName("status") var status: String? = null
    @SerializedName("status_message") var statusMessage: String? = null
    @SerializedName("data") var data: T? = null
}