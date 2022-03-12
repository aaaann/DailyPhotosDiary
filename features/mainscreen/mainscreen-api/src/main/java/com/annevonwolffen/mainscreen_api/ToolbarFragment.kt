package com.annevonwolffen.mainscreen_api

import android.view.Menu
import android.view.MenuItem

interface ToolbarFragment {

    fun inflateToolbarMenu(
        menuRes: Int,
        prepareOptionsMenu: ((Menu) -> Unit)? = null,
        onMenuItemClickListener: (MenuItem) -> Boolean
    )

    fun clearToolbarMenu()
}