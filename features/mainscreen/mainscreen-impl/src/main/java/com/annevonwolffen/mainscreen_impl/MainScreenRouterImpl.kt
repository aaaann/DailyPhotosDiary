package com.annevonwolffen.mainscreen_impl

import android.app.Activity
import android.content.Intent
import com.annevonwolffen.mainscreen_api.MainScreenRouter
import javax.inject.Inject

internal class MainScreenRouterImpl @Inject constructor() : MainScreenRouter {
    override fun openMainScreen(activity: Activity) {
        activity.startActivity(Intent(activity, MainScreenActivity::class.java))
    }
}