package com.annevonwolffen.design_system.extensions

import android.app.Activity

fun Activity.removeNavBarInset(listener: OnSystemInsetsChangedListener = { _, _ -> }) {
    window.decorView.removeNavBarInset(listener)
}