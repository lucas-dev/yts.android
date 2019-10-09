package com.alvefard.yify.presentation.common.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import butterknife.ButterKnife
import com.bluelinelabs.conductor.Controller

abstract class BaseController : Controller {
    protected constructor()

    protected constructor(args: Bundle) : super(args)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(getLayoutId(), container, false)
        ButterKnife.bind(this, view)
        onCreateView()
        return view
    }

    override fun onContextAvailable(context: Context) {
        super.onContextAvailable(context)
        injectDependencies()
    }

    override fun onDestroy() {
        getPresenter()?.let {
            it.detachView()
            it.destroy()
        }
        super.onDestroy()
    }

    protected abstract fun getPresenter(): BaseContract.Presenter<*>?
    @LayoutRes protected abstract fun getLayoutId(): Int
    protected abstract fun injectDependencies()
    protected abstract fun onCreateView()

}