package com.alvefard.yify.presentation.common.ux.screennavigator

import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.alvefard.yify.domain.model.Movie
import com.alvefard.yify.presentation.common.Constants
import com.alvefard.yify.presentation.screens.about.AboutScreen
import com.alvefard.yify.presentation.screens.details.DetailsScreen
import com.alvefard.yify.presentation.screens.favs.FavsScreen
import com.alvefard.yify.presentation.screens.listing.ListingScreen
import com.alvefard.yify.presentation.screens.moviespager.MoviesPagerScreen
import com.alvefard.yify.presentation.screens.search.SearchScreen
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.alvefard.yify.presentation.screens.details.transitions.DetailPopAnimChangeHandler
import com.alvefard.yify.presentation.screens.details.transitions.DetailPopTransChangeHandler
import com.alvefard.yify.presentation.screens.details.transitions.DetailPushAnimChangeHandler
import com.alvefard.yify.presentation.screens.details.transitions.DetailPushTransChangeHandler
import com.bluelinelabs.conductor.changehandler.TransitionChangeHandlerCompat



class ScreenNavigatorImpl(private val router: Router, private val navDrawer: DrawerLayout) : ScreenNavigatorConductor {
    private var adapterRouter: Router? = null

    override fun setAdapterRouter(router: Router) {
        adapterRouter = router
    }

    override fun attachRouter(): Boolean {
        return router.handleBack()
    }

    override fun presentMainScreen() {
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(MoviesPagerScreen()))
        }
    }


    override fun toLatestMoviesPage() {
        adapterRouter?.setRoot(RouterTransaction.with(ListingScreen(Constants.MovieType.LATEST)))
    }

    override fun toPopularMoviesPage() {
        adapterRouter?.setRoot(RouterTransaction.with(ListingScreen(Constants.MovieType.POPULAR)))
    }

    override fun toTopRatedMoviesPage() {
        adapterRouter?.setRoot(RouterTransaction.with(ListingScreen(Constants.MovieType.TOP_RATED)))
    }

    override fun toDetailsPage(movie: Movie) {
        val pushHandler = TransitionChangeHandlerCompat(DetailPushTransChangeHandler(movie.id),
                DetailPushAnimChangeHandler())
        val popHandler = TransitionChangeHandlerCompat(DetailPopTransChangeHandler(movie.id),
                DetailPopAnimChangeHandler())
        router.pushController(RouterTransaction.with(DetailsScreen(movie))
                .pushChangeHandler(pushHandler)
                .popChangeHandler(popHandler))
    }

    override fun toSearchPage() {
        router.pushController(RouterTransaction.with(SearchScreen()))
    }

    override fun toFavsPage() {
        router.pushController(RouterTransaction.with(FavsScreen()))
    }

    override fun toAboutPage() {
        router.pushController(RouterTransaction.with(AboutScreen()))
    }

    override fun goBack() {
        router.popCurrentController()
    }

    override fun popToRoot() {
        router.popToRoot()
    }

    override fun openSideMenu() {
        navDrawer.openDrawer(GravityCompat.START)
    }

    override fun lockSideMenu() {
        navDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    override fun unlockSideMenu() {
        navDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

}