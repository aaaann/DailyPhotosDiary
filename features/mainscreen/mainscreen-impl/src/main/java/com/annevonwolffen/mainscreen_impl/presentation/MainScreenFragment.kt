package com.annevonwolffen.mainscreen_impl.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.annevonwolffen.authorization_api.di.AuthorizationApi
import com.annevonwolffen.di.FeatureProvider
import com.annevonwolffen.mainscreen_impl.R
import com.google.android.material.navigation.NavigationView

class MainScreenFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

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

        setUpAppbar(view)
        setUpNavView(view)
    }

    private fun setUpNavView(view: View) {
        val sideNavView: NavigationView = view.findViewById(R.id.nav_view)
        sideNavView.setupWithNavController(navController)

    }

    private fun setUpAppbar(view: View) {
        val drawer: DrawerLayout = view.findViewById(R.id.drawer_layout)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                com.annevonwolffen.gallery_api.R.id.gallery_fragment,
                com.annevonwolffen.settings_api.R.id.settings_fragment
            ),
            drawer
        )
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.setupWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == com.annevonwolffen.navigation.R.id.authorization_graph) {
                FeatureProvider.getFeature(AuthorizationApi::class.java).authInteractor.signOut()
                navController.popBackStack()
                // requireActivity().findNavController(R.id.nav_host_fragment_content_main_screen)
                //     .navigate(R.id.action_global_authorization)
            }
        }
    }
}

