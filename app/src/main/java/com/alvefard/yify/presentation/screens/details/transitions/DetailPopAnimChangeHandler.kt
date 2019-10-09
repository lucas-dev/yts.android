package com.alvefard.yify.presentation.screens.details.transitions

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import android.view.ViewGroup
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import com.bluelinelabs.conductor.changehandler.AnimatorChangeHandler


class DetailPopAnimChangeHandler : AnimatorChangeHandler() {
    override fun resetFromView(from: View) {
        // TODO: get view references the right way
        val ivBackground = ((((from as ViewGroup).getChildAt(0) as ViewGroup).getChildAt(5) as ViewGroup).getChildAt(0) as ViewGroup).getChildAt(1)
        val ivCover = ((((from.getChildAt(0) as ViewGroup).getChildAt(5) as ViewGroup).getChildAt(0) as ViewGroup).getChildAt(2) as ViewGroup)
        val btnFav = (((from.getChildAt(0) as ViewGroup).getChildAt(5) as ViewGroup).getChildAt(0) as ViewGroup).getChildAt(3)
        ivBackground.scaleX = 1f
        ivBackground.scaleY = 1f
        ivCover.translationY = 0f
        btnFav.translationY = 0f
    }

    override fun getAnimator(container: ViewGroup, from: View?, to: View?, isPush: Boolean, toAddedToContainer: Boolean): Animator {
        val ivBackground = ((((from as ViewGroup).getChildAt(0) as ViewGroup).getChildAt(5) as ViewGroup).getChildAt(0) as ViewGroup).getChildAt(1)
        val ivCover = ((((from.getChildAt(0) as ViewGroup).getChildAt(5) as ViewGroup).getChildAt(0) as ViewGroup).getChildAt(2) as ViewGroup)
        val btnFav = (((from.getChildAt(0) as ViewGroup).getChildAt(5) as ViewGroup).getChildAt(0) as ViewGroup).getChildAt(3)

        val animatorSet = AnimatorSet()

        // Set the to View's alpha to 0 to hide it at the beginning.
        to!!.alpha = 0f

        // Scale down to hide the fab button
        val fabScaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0f)
        val fabScaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f)
        val hideFabButtonAnimator = ObjectAnimator.ofPropertyValuesHolder(btnFav, fabScaleX, fabScaleY)

        // Slide up the cover
        val coverAnimator = ObjectAnimator.ofFloat(ivBackground, View.TRANSLATION_Y, 0f, -ivCover.height.toFloat())

        // Slide down the details
        val detailAnimator = ObjectAnimator.ofFloat(btnFav, View.TRANSLATION_Y, 0f, btnFav.height.toFloat())

        // Show the new view
        val showToViewAnimator = ObjectAnimator.ofFloat(to, View.ALPHA, 0f, 1f)

        animatorSet.playTogether(hideFabButtonAnimator, coverAnimator, detailAnimator, showToViewAnimator)
        animatorSet.duration = 300
        animatorSet.interpolator = FastOutLinearInInterpolator()

        animatorSet.start()

        return animatorSet
    }

}