package com.dpw.nimons360.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dpw.nimons360.R
import com.dpw.nimons360.core.utils.SessionManager
import com.dpw.nimons360.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        setSupportActionBar(binding.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        val navGraph = navController.navInflater.inflate(R.navigation.main_nav_graph)
        navGraph.setStartDestination(
            if (sessionManager.isLoggedIn()) R.id.homeFragment else R.id.loginFragment
        )
        navController.graph = navGraph

        binding.bottomNavigation.setupWithNavController(navController)

        binding.avatarButton.setOnClickListener {
            if (navController.currentDestination?.id != R.id.profileFragment) {
                navController.navigate(R.id.profileFragment)
            }
        }

        binding.createFamilyFab.setOnClickListener {
            navController.navigate(R.id.createFamilyFragment)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isLoggedOutScreen = destination.id == R.id.loginFragment
            binding.toolbarContainer.isVisible = !isLoggedOutScreen
            binding.bottomNavigation.isVisible = !isLoggedOutScreen
            binding.createFamilyFab.isVisible = !isLoggedOutScreen

            binding.toolbar.title = when (destination.id) {
                R.id.homeFragment -> getString(R.string.title_home)
                R.id.mapFragment -> getString(R.string.title_map)
                R.id.familiesFragment -> getString(R.string.title_families)
                R.id.profileFragment -> getString(R.string.title_profile)
                R.id.createFamilyFragment -> getString(R.string.title_create_family)
                else -> getString(R.string.app_name)
            }

            binding.avatarButton.isVisible = destination.id != R.id.profileFragment
            binding.createFamilyFab.isVisible =
                !isLoggedOutScreen && destination.id != R.id.createFamilyFragment
        }
    }
}