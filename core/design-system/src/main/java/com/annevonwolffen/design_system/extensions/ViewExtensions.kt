package com.annevonwolffen.design_system.extensions

import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

typealias OnSystemInsetsChangedListener = (statusBarSize: Int, navigationBarSize: Int) -> Unit

fun View.removeNavBarInset(listener: OnSystemInsetsChangedListener) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->

        val topInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top
        val bottomInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom

        listener(topInset, bottomInset)

        ViewCompat.onApplyWindowInsets(
            this,
            WindowInsetsCompat.Builder(insets).setInsets(
                WindowInsetsCompat.Type.systemBars(),
                Insets.of(0, topInset, 0, 0)
            ).build()
        )
    }
}