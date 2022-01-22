package com.annevonwolffen.authorization_impl

import android.app.Activity
import android.content.Intent
import com.annevonwolffen.authorization_api.AuthorizationScreenRouter
import javax.inject.Inject

internal class AuthorizationScreenRouterImpl @Inject constructor() : AuthorizationScreenRouter {
    override fun openAuthorizationScreen(activity: Activity) {
        activity.startActivity(Intent(activity, AuthorizationActivity::class.java))
    }
}