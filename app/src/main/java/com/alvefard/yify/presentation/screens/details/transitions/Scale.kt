package com.alvefard.yify.presentation.screens.details.transitions

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.transition.TransitionValues
import android.transition.Visibility
import android.util.AttributeSet
import android.view.View
import android.view.View.SCALE_X
import android.view.View.SCALE_Y
import android.view.ViewGroup


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class Scale : Visibility {

    constructor()

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onAppear(sceneRoot: ViewGroup, view: View, startValues: TransitionValues?, endValues: TransitionValues?): Animator {
        return getAnimator(view, 0f, 1f)
    }

    override fun onDisappear(sceneRoot: ViewGroup, view: View, startValues: TransitionValues?, endValues: TransitionValues?): Animator {
        return getAnimator(view, 1f, 0f)
    }

    private fun getAnimator(view: View, startValue: Float, endValue: Float): Animator {
        view.scaleX = startValue
        view.scaleY = startValue
        val scaleX = PropertyValuesHolder.ofFloat(SCALE_X, startValue, endValue)
        val scaleY = PropertyValuesHolder.ofFloat(SCALE_Y, startValue, endValue)
        return ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY)
    }
}