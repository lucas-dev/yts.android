package com.alvefard.yify.presentation.screens.details.transitions

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.transition.*
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import com.bluelinelabs.conductor.changehandler.TransitionChangeHandler


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class DetailPopTransChangeHandler : TransitionChangeHandler {
    override fun getTransition(container: ViewGroup, from: View?, to: View?, isPush: Boolean): Transition {
        // TODO: get view references the right way
        val ivCover = (((((from as ViewGroup).getChildAt(0) as ViewGroup).getChildAt(5) as ViewGroup).getChildAt(0) as ViewGroup).getChildAt(2) as ViewGroup)
        val btnFav = (((from.getChildAt(0) as ViewGroup).getChildAt(5) as ViewGroup).getChildAt(0) as ViewGroup).getChildAt(3)

        ivCover.transitionName = coverViewTransitionName

        return TransitionSet()
                .addTransition(TransitionSet()
                        .addTransition(ChangeBounds())
                        .addTransition(ChangeClipBounds())
                        .addTransition(ChangeTransform())
                        .addTransition(ChangeImageTransform()))
                .addTransition(Scale().addTarget(btnFav))
    }

    private var coverViewTransitionName: String? = null

    constructor() {
        this.coverViewTransitionName = null
    }

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