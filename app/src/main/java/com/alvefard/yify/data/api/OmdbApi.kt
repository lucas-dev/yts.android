package com.alvefard.yify.data.api

import com.alvefard.yify.data.entities.omdb.OmdbSchema
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {
    @GET(".")
    fun getOmdbDetails(@Query("i") i: String): Single<OmdbSchema>
}
