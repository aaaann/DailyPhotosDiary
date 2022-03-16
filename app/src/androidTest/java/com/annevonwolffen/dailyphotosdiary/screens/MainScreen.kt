package com.annevonwolffen.dailyphotosdiary.screens

import com.kaspersky.kaspresso.screens.KScreen
import com.annevonwolffen.mainscreen_impl.R
import com.annevonwolffen.mainscreen_impl.presentation.MainScreenFragment
import io.github.kakaocup.kakao.drawer.KDrawerView
import io.github.kakaocup.kakao.toolbar.KToolbar

object MainScreen : KScreen<MainScreen>() {
    override val layoutId: Int? = R.layout.fragment_main_screen
    override val viewClass: Class<*>? = MainScreenFragment::class.java

    val drawer = KDrawerView { withId(R.id.drawer_layout) }
    val toolbar = KToolbar { withId(R.id.toolbar) }

}