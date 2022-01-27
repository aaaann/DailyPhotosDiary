package com.annevonwolffen.mainscreen_impl.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.annevonwolffen.mainscreen_impl.R
import com.annevonwolffen.mainscreen_impl.databinding.ActivityMainScreenBinding
import com.google.android.material.navigation.NavigationView

class MainScreenActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main_screen) as NavHostFragment?
                ?: return
        navController = navHostFragment.navController

        setUpToolbar()
        setUpNavView()
    }

    private fun setUpNavView() {
        val sideNavView: NavigationView = binding.navView
        sideNavView.setupWithNavController(navController)
    }

    private fun setUpToolbar() {
        val drawer: DrawerLayout = binding.drawerLayout
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.gallery_graph),
            drawer
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment_content_main_screen).navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}