package com.annevonwolffen.design_system.extensions

import android.app.Activity
import androidx.annotation.ColorRes

fun Activity.removeNavBarInset(listener: OnSystemInsetsChangedListener = { _, _ -> }) {
    window.decorView.removeNavBarInset(listener)
}

fun Activity.setStatusBarColor(@ColorRes colorId: Int) {
    window.statusBarColor = resources.getColor(colorId, theme)
}