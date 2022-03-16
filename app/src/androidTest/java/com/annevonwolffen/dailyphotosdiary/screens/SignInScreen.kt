package com.annevonwolffen.dailyphotosdiary.screens

import com.kaspersky.kaspresso.screens.KScreen
import com.annevonwolffen.authorization_impl.R
import com.annevonwolffen.authorization_impl.presentation.SignInFragment
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.text.KButton

object SignInScreen : KScreen<SignInScreen>() {
    override val layoutId: Int? = R.layout.fragment_sign_in
    override val viewClass: Class<*>? = SignInFragment::class.java

    val signInButton = KButton { withId(R.id.btn_sign_in) }
    val emailEditText = KEditText { withId(R.id.et_email) }
    val passwordEditText = KEditText { withId(R.id.et_password) }
    val noAccountButton = KButton { withId(R.id.btn_no_account) }
    val progressLayout = KView { withId(R.id.progress_layout) }

    fun enterEmailAndPassword(email: String, password: String) {
        emailEditText { typeText(email) }
        passwordEditText { typeText(password) }
        emailEditText { hasText(email) }
        passwordEditText { hasText(password) }
    }

    fun clearEmailAndPassword() {
        emailEditText { clearText() }
        passwordEditText { clearText() }
        emailEditText { hasEmptyText() }
        passwordEditText { hasEmptyText() }
    }
}