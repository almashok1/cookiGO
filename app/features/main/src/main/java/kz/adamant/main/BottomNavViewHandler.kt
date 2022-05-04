package kz.adamant.main

import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BottomNavViewHandler {

    private fun isVisible(destination: NavDestination): Boolean {
        return when (destination.id) {
            kz.adamant.home.R.id.homeFragment,
            kz.adamant.account.R.id.accountFragment,
            kz.adamant.favourites.R.id.favouritesFragment,
            kz.adamant.checklist.R.id.checklistFragment -> true
            else -> false
        }
    }

    fun handleBottomNav(
        navController: NavController,
        bottomNavigationView: View,
        floatingActionButton: FloatingActionButton,
        onBottomNavVisible: (Boolean) -> Unit
    ) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isVisible = isVisible(destination)
            onBottomNavVisible(isVisible)
            bottomNavigationView.isVisible = isVisible
            if (isVisible) floatingActionButton.show() else floatingActionButton.hide()
        }
    }
}
