package com.annevonwolffen.dailyphotosdiary.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updatePadding
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.annevonwolffen.dailyphotosdiary.R
import com.annevonwolffen.design_system.extensions.removeNavBarInset
import com.annevonwolffen.mainscreen_impl.databinding.ActivityMainScreenBinding

class StartActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        removeNavBarInset { _: Int, navigationBarSize: Int ->
            binding.root.updatePadding(bottom = navigationBarSize)
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
                ?: return
        navController = navHostFragment.navController
    }
}