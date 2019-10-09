package com.alvefard.yify.presentation.common.ux.screennavigator

import com.bluelinelabs.conductor.Router

interface ScreenNavigatorConductor : ScreenNavigator {
    fun setAdapterRouter(router: Router)
    fun attachRouter(): Boolean
}
