package com.alvefard.yify.presentation.common.rx

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable

class RxBus {
    private val bus = PublishRelay.create<Any>().toSerialized()

    fun send(event: Any) = bus.accept(event)

    fun toObservable() = bus

    fun hasObservers() = bus.hasObservers()
}
