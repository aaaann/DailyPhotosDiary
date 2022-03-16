package com.annevonwolffen.dailyphotosdiary.screens

import com.kaspersky.kaspresso.screens.KScreen
import com.annevonwolffen.authorization_impl.R
import com.annevonwolffen.authorization_impl.presentation.SignUpFragment
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.text.KButton

object SignUpScreen : KScreen<SignUpScreen>() {
    override val layoutId: Int? = R.layout.fragment_sign_up
    override val viewClass: Class<*>? = SignUpFragment::class.java

    val signUpButton = KButton { withId(R.id.btn_sign_up) }
    val emailEditText = KEditText { withId(R.id.et_email) }
    val passwordEditText = KEditText { withId(R.id.et_password) }
    val hasAccountButton = KButton { withId(R.id.btn_has_account) }
    val progressLayout = KView { withId(R.id.progress_layout) }
}