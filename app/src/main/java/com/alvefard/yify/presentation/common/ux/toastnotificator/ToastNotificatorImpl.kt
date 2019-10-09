package com.alvefard.yify.presentation.common.ux.toastnotificator

import android.app.Activity
import android.widget.Toast

class ToastNotificatorImpl(internal var activity: Activity): ToastNotificator {

    override fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}
