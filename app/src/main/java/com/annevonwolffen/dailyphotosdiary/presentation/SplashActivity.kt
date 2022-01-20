package com.annevonwolffen.dailyphotosdiary.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.annevonwolffen.authorization_api.AuthorizationApi
import com.annevonwolffen.dailyphotosdiary.R
import com.annevonwolffen.di.FeatureProvider.getFeature

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getFeature(AuthorizationApi::class.java).authorizationRouter.openAuthorizationScreen(this)
    }
}