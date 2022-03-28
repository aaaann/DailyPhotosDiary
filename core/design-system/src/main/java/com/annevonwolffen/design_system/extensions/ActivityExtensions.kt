package com.annevonwolffen.design_system.extensions

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes

fun Activity.removeNavBarInset(listener: OnSystemInsetsChangedListener = { _, _ -> }) {
    window.decorView.removeNavBarInset(listener)
}

fun Activity.setStatusBarColor(@ColorRes colorId: Int) {
    window.statusBarColor = resources.getColor(colorId, theme)
}

fun Activity.hideKeyboard() {
    currentFocus?.let { view ->
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}