package com.annevonwolffen.authorization_impl

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.annevonwolffen.di.FeatureProvider
import com.annevonwolffen.mainscreen_api.MainScreenApi

internal class AuthorizationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)
        findViewById<Button>(R.id.btn_start_main).setOnClickListener {
            FeatureProvider.getFeature(MainScreenApi::class.java)
                .mainScreenRouter.openMainScreen(this)
        }
    }
}