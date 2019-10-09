package com.alvefard.yify.data.utils

import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

fun <T> Single<T>.mapNetworkErrors(): Single<T> =
        this.onErrorResumeNext { error ->
            when (error) {
                is SocketTimeoutException -> Single.error(NoNetworkException(error))
                is UnknownHostException -> Single.error(ServerUnreachableException(error))
                is HttpException -> Single.error(HttpCallFailureException(error))
                else -> Single.error(error)
            }
        }


fun <T> Single<T>.retryIfNeeded(): Single<T> =
        this.onErrorResumeNext {
            this.retryWhen { errors ->
                errors.take(3).flatMap { error ->
                    when (error) {
                        is NoNetworkException, is ServerUnreachableException, is HttpCallFailureException -> Flowable.just("retry").delay(10, TimeUnit.SECONDS)
                        else -> Flowable.error(error)
                    }
                }
            }
        }
