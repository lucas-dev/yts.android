package com.alvefard.yify.presentation.screens.details.transitions

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.transition.*
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.bluelinelabs.conductor.changehandler.TransitionChangeHandler


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class DetailPushTransChangeHandler : TransitionChangeHandler {
    override fun getTransition(container: ViewGroup, from: View?, to: View?, isPush: Boolean): Transition {
        // TODO: get view references the right way
        val ivBackground = ((((to as ViewGroup).getChildAt(0) as ViewGroup).getChildAt(5) as ViewGroup).getChildAt(0) as ViewGroup).getChildAt(1)
        val ivCover = ((((to.getChildAt(0) as ViewGroup).getChildAt(5) as ViewGroup).getChildAt(0) as ViewGroup).getChildAt(2) as ViewGroup)
        val btnFav = (((to.getChildAt(0) as ViewGroup).getChildAt(5) as ViewGroup).getChildAt(0) as ViewGroup).getChildAt(3)
        ivCover.transitionName = coverViewTransitionName

        val changeTransform = ChangeTransform()

        changeTransform.reparentWithOverlay = false

        return TransitionSet()
                .addTransition(TransitionSet()
                        .addTransition(ChangeBounds())
                        .addTransition(ChangeClipBounds())
                        .addTransition(changeTransform)
                        .addTransition(ChangeImageTransform())
                        .setDuration(300))
                .addTransition(Slide().addTarget(ivBackground).setStartDelay(150))
                .addTransition(Scale().addTarget(btnFav).setStartDelay(300))
                .setInterpolator(FastOutSlowInInterpolator())
    }

    private var coverViewTransitionName: String? = null

    constructor() {}


    constructor(coverViewTransitionName: String) {
        this.coverViewTransitionName = coverViewTransitionName
    }

    override fun saveToBundle(@NonNull bundle: Bundle) {
        bundle.putString(KEY_COVER_TRANSITION_NAME, coverViewTransitionName)
    }

    override fun restoreFromBundle(@NonNull bundle: Bundle) {
        coverViewTransitionName = bundle.getString(KEY_COVER_TRANSITION_NAME)
    }

    companion object {
        private const val KEY_COVER_TRANSITION_NAME = "key_cover_transition_name"
    }
}