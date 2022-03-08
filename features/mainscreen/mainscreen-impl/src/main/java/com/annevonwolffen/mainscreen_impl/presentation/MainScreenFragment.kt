package com.annevonwolffen.mainscreen_impl.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.annevonwolffen.authorization_api.di.AuthorizationApi
import com.annevonwolffen.di.FeatureProvider
import com.annevonwolffen.mainscreen_impl.R
import com.annevonwolffen.navigation.activityNavController
import com.annevonwolffen.navigation.navigateSafely
import com.google.android.material.navigation.NavigationView
import com.annevonwolffen.gallery_api.R as GalleryR
import com.annevonwolffen.navigation.R as NavigationR
import com.annevonwolffen.settings_api.R as SettingsR

class MainScreenFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawer: DrawerLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_main_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main_screen) as NavHostFragment?
                ?: return

        navController = navHostFragment.navController

        drawer = view.findViewById(R.id.drawer_layout)

        setUpAppbar(view)
        setUpNavView(view)
    }

    private fun setUpNavView(view: View) {
        val sideNavView: NavigationView = view.findViewById(R.id.nav_view)
        sideNavView.setupWithNavController(navController)

        sideNavView.setNavigationItemSelectedListener { menuItem ->
            if (menuItem.itemId == R.id.log_out) {
                FeatureProvider.getFeature(AuthorizationApi::class.java).authInteractor.signOut()
                activityNavController().navigateSafely(NavigationR.id.action_global_authorization)
            } else {
                NavigationUI.onNavDestinationSelected(menuItem, navController)
                drawer.closeDrawer(GravityCompat.START)
            }

            true
        }
    }

    private fun setUpAppbar(view: View) {
        appBarConfiguration = AppBarConfiguration(
            setOf(
                GalleryR.id.gallery_fragment,
                SettingsR.id.settings_fragment
            ),
            drawer
        )
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }
}
