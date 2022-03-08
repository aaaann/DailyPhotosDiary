package com.annevonwolffen.mainscreen_api

import android.view.MenuItem

interface ToolbarFragment {

    fun inflateToolbarMenu(menuRes: Int, onMenuItemClickListener: (MenuItem) -> Boolean)
}