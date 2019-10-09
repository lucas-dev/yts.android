package com.alvefard.yify.presentation.di

import com.alvefard.yify.data.api.OmdbApi
import com.alvefard.yify.data.api.YifyApi
import com.alvefard.yify.presentation.screens.landing.di.LandingScope
import com.alvefard.yifymovies.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
class NetworkModule {

    @Provides
    @LandingScope
    @Named("yify_retrofit")
    fun provideYifyRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://yts.am/api/v2/")
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    @Provides
    @LandingScope
    @Named("omdb_retrofit")
    fun provideOmdbRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl("http://www.omdbapi.com/")
                .client(client.newBuilder().addInterceptor { chain ->
                    var request = chain.request()
                    val url = request.url().newBuilder().addQueryParameter(
                            "apikey",
                            BuildConfig.OMDB_APIKEY
                    ).build()
                    request = request.newBuilder().url(url).build()
                    chain.proceed(request)
                }.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    @Provides
    @LandingScope
    fun provideClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Provides
    @LandingScope
    fun provideOmdbApiClient(@Named("omdb_retrofit") retrofit: Retrofit): OmdbApi {
        return retrofit.create(OmdbApi::class.java)
    }

    @Provides
    @LandingScope
    fun provideYifyApiClient(@Named("yify_retrofit") retrofit: Retrofit): YifyApi {
        return retrofit.create(YifyApi::class.java)
    }

}