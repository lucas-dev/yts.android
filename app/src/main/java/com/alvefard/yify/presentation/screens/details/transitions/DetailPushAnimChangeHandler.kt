package com.alvefard.yify.presentation.screens.details.transitions

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.bluelinelabs.conductor.changehandler.AnimatorChangeHandler


class DetailPushAnimChangeHandler : AnimatorChangeHandler() {
    override fun getAnimator(container: ViewGroup, from: View?, to: View?, isPush: Boolean, toAddedToContainer: Boolean): Animator {
        // TODO: get view references the right way
        val ivCover = ((((to as ViewGroup).getChildAt(0) as ViewGroup).getChildAt(5) as ViewGroup).getChildAt(0) as ViewGroup).getChildAt(2)
        val btnFav = (((to.getChildAt(0) as ViewGroup).getChildAt(5) as ViewGroup).getChildAt(0) as ViewGroup).getChildAt(3)
        val ivBackground = (to.getChildAt(0) as ViewGroup).getChildAt(4)
        // Set the button scale to 0 to make it invisible at the beginning.
        ivBackground.scaleX = 0f
        ivBackground.scaleY = 0f

        val animatorSet = AnimatorSet()

        val coverAndDetailAnim = AnimatorSet()

        // Hide the old view
        val hideFromViewAnim = ObjectAnimator.ofFloat(from, View.ALPHA, 1f, 0f)

        // Slide down the cover
        val coverAnim = ObjectAnimator.ofFloat(ivCover, TRANSLATION_Y, -ivCover.height.toFloat(), 0f)

        // Slide up the details
        val detailAnim = ObjectAnimator.ofFloat(btnFav, TRANSLATION_Y, btnFav.height.toFloat(), 0f)

        coverAndDetailAnim.playTogether(hideFromViewAnim, coverAnim, detailAnim)
        coverAndDetailAnim.duration = 300
        coverAndDetailAnim.interpolator = FastOutSlowInInterpolator()

        // Scale up the favourite fab
        val fabScaleX = PropertyValuesHolder.ofFloat(SCALE_X, 0f, 1f)
        val fabScaleY = PropertyValuesHolder.ofFloat(SCALE_Y, 0f, 1f)
        val favouriteAnim = ObjectAnimator.ofPropertyValuesHolder(ivBackground, fabScaleX, fabScaleY)
                .setDuration(200)

        animatorSet.playSequentially(coverAndDetailAnim, favouriteAnim)

        animatorSet.start()

        return animatorSet
    }

    override fun resetFromView(from: View) {
        from.alpha = 1f
    }
}