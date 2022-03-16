package com.annevonwolffen.dailyphotosdiary.screens

import com.annevonwolffen.authorization_impl.presentation.AuthorizationFragment
import com.kaspersky.kaspresso.screens.KScreen
import com.annevonwolffen.authorization_impl.R
import io.github.kakaocup.kakao.toolbar.KToolbar

object BaseAuthScreen : KScreen<BaseAuthScreen>() {
    override val layoutId: Int? = R.layout.fragment_auth
    override val viewClass: Class<*>? = AuthorizationFragment::class.java

    val toolbar = KToolbar { withId(R.id.toolbar) }
}