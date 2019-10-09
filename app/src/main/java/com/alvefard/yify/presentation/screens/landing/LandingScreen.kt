package com.alvefard.yify.presentation.screens.landing

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.alvefard.yify.presentation.App
import com.alvefard.yify.presentation.common.ux.screennavigator.ScreenNavigatorConductor
import com.alvefard.yifymovies.R
import com.bluelinelabs.conductor.Conductor
import com.google.android.material.navigation.NavigationView
import javax.inject.Inject

class LandingScreen : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    @Inject
    lateinit var screenNavigator: ScreenNavigatorConductor

    @BindView(R.id.activity_main)
    lateinit var mDrawerLayout: DrawerLayout
    @BindView(R.id.nv)
    lateinit var mNavigationView: NavigationView
    @BindView(R.id.container)
    lateinit var mContainer: ViewGroup

    private lateinit var mActionBarDrawerToggle: ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT

        setContentView(R.layout.activity_main)

        ButterKnife.bind(this)

        (application as App)
                .createLandingComponent(this,
                        Conductor.attachRouter(this, mContainer, savedInstanceState), mDrawerLayout).inject(this)

        screenNavigator.presentMainScreen()

        setUpNavigationDrawer()

    }

    override fun onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeNavDrawer()
        } else {
            if (!screenNavigator.attachRouter())
                super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.movies -> screenNavigator.popToRoot()
            R.id.search -> screenNavigator.toSearchPage()
            R.id.favs -> screenNavigator.toFavsPage()
            R.id.about -> screenNavigator.toAboutPage()
        }
        closeNavDrawer()

        return false
    }

    private fun closeNavDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun setUpNavigationDrawer() {
        mActionBarDrawerToggle = object : ActionBarDrawerToggle(this, mDrawerLayout, R.string.open,
                R.string.close) {
            private val scaleFactor = 6f

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                val slideX = drawerView.width * slideOffset
                mContainer.translationX = slideX
                mContainer.scaleX = 1 - slideOffset / scaleFactor
                mContainer.scaleY = 1 - slideOffset / scaleFactor
            }
        }

        mDrawerLayout.setScrimColor(Color.TRANSPARENT)
        mDrawerLayout.drawerElevation = 0f
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle)
        mActionBarDrawerToggle.syncState()


        mNavigationView.setNavigationItemSelectedListener(this)
    }

}