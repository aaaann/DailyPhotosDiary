package com.annevonwolffen.ui_utils_api.extensions

import android.view.View

fun View.setVisibility(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}