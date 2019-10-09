package com.alvefard.yify.presentation.common.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BasePresenter<T : BaseContract.View> : BaseContract.Presenter<T> {
    var view: T? = null
        private set
    private val disposables = CompositeDisposable()

    override fun attachView(view: T) {
        this.view = view
    }

    override fun detachView() {
        view = null
    }

    override fun destroy() {
        if (!disposables.isDisposed)
            disposables.clear()
    }


    protected fun addSubscription(subscription: Disposable) {
        disposables.add(subscription)
    }

}