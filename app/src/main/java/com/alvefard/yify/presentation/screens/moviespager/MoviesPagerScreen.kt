package com.alvefard.yify.presentation.screens.moviespager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ViewFlipper
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.alvefard.yify.presentation.App
import com.alvefard.yify.presentation.common.ux.screennavigator.ScreenNavigatorConductor
import com.alvefard.yify.presentation.util.EnhanceTabLayout
import com.alvefard.yifymovies.R
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.support.RouterPagerAdapter
import com.google.android.material.tabs.TabLayout
import javax.inject.Inject

class MoviesPagerScreen : Controller() {
    @Inject
    lateinit var screenNavigator: ScreenNavigatorConductor

    @BindView(R.id.viewpager)
    lateinit var mViewPager: ViewPager

    @BindView(R.id.toolbar)
    lateinit var toolbar: ViewGroup

    @BindView(R.id.tablayout)
    lateinit var tabLayout: EnhanceTabLayout

    private val sTitle = arrayOf("Newest", " Top rated ", "Popular")

    init {
        retainViewMode = RetainViewMode.RETAIN_DETACH
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.fragment_pager_movies, container, false)

        ButterKnife.bind(this, view)

        (activity!!.application as App).createMoviesPagerComponent(toolbar).inject(this)

        mViewPager.adapter = object : RouterPagerAdapter(this) {
            override fun configureRouter(router: Router, position: Int) {
                screenNavigator.setAdapterRouter(router)
                if (!router.hasRootController()) {
                    when (position) {
                        0 -> screenNavigator.toLatestMoviesPage()
                        1 -> screenNavigator.toTopRatedMoviesPage()
                        2 -> screenNavigator.toPopularMoviesPage()
                        else -> screenNavigator.toPopularMoviesPage()
                    }
                }
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return sTitle[position]
            }

            override fun getCount(): Int {
                return 3
            }
        }
        mViewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                (toolbar.findViewById<View>(R.id.vs_filter) as ViewFlipper).displayedChild = position
            }
        })
        mViewPager.offscreenPageLimit = 3

        for (i in sTitle.indices) {
            tabLayout.addTab(sTitle[i])
        }
        tabLayout.setupWithViewPager(mViewPager)

        mViewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout.tabLayout))

        return view
    }

    @OnClick(R.id.menu)
    internal fun openSideMenu() {
        screenNavigator.openSideMenu()
    }
}
