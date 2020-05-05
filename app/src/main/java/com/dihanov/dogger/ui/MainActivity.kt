package com.dihanov.dogger.ui

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dihanov.base_ui.BaseActivity
import com.dihanov.dogger.R
import com.dihanov.util.KeyboardManager
import kotlinx.android.synthetic.main.main_activity.*
import org.koin.android.ext.android.inject


class MainActivity : BaseActivity() {
    private lateinit var navController: NavController
    private val keyboardManager: KeyboardManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        navController = findNavController(R.id.nav_host_fragment)
        setUpViews()

        navigation.setOnNavigationItemSelectedListener { item ->
            keyboardManager.hideKeyboard(container)
            when (item.itemId) {
                R.id.dog_search -> {
                    navigateWithPop(R.id.dogSearchFragment)
                    true
                }
                R.id.cat_search -> {
                    navigateWithPop(R.id.catSearchFragment)
                    true
                }
                R.id.combined_search -> {
                    navigateWithPop(R.id.combinedFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun navigateWithPop(destinationId: Int) {
        navController.popBackStack(R.id.nav_graph, true)
        navController.navigate(destinationId)
    }

    private fun setUpViews() {
        val topLevelDestinations: MutableSet<Int> = HashSet()
        topLevelDestinations.addAll(listOf(
            R.id.dogSearchFragment,
            R.id.catSearchFragment,
            R.id.combinedFragment
        ))

        val appBarConfiguration = AppBarConfiguration.Builder(topLevelDestinations)
            .build()

        navigation.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }


    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp()
    }
}
