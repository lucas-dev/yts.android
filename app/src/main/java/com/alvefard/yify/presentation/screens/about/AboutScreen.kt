package com.alvefard.yify.presentation.screens.about

import butterknife.OnClick
import com.alvefard.yify.presentation.App
import com.alvefard.yify.presentation.common.base.BaseContract
import com.alvefard.yify.presentation.common.base.BaseController
import com.alvefard.yify.presentation.common.ux.screennavigator.ScreenNavigatorConductor
import com.alvefard.yifymovies.R
import javax.inject.Inject


class AboutScreen : BaseController() {

    @Inject
    lateinit var screenNavigator: ScreenNavigatorConductor

    override fun getPresenter(): BaseContract.Presenter<*>? {
        return null
    }

    override fun getLayoutId(): Int {
        return R.layout.about_screen
    }

    override fun injectDependencies() {
        App.moviesPagerComponent.inject(this)
    }

    override fun onCreateView() {}

    @OnClick(R.id.menu)
    fun onMenuClick() {
        screenNavigator.openSideMenu()
    }

}