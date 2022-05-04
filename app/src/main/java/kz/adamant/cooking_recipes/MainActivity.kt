package kz.adamant.cooking_recipes

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.adamant.common.AppMessages
import kz.adamant.common.BaseViewModel
import kz.adamant.common.EventObserver
import kz.adamant.common.binding.BindingActivity
import kz.adamant.common.extensions.getNavigationBarsHeight
import kz.adamant.common.extensions.makeSnackBar
import kz.adamant.cooking_recipes.databinding.ActivityMainBinding
import kz.adamant.data.managers.SessionManager
import kotlin.reflect.KClass

class MainActivity: BindingActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private lateinit var navController: NavController

    private val navBarHeight by lazy {
        getNavigationBarsHeight()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpNavigation()
    }

    private fun setUpNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController

        SessionManager.loggedOut.observe(this) {
            if (it) navController.navigate(R.id.action_global_auth_screen)
        }

        AppMessages.snackbarMessages.observe(this, EventObserver { pair ->
            val f = supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.lastOrNull()
            val root = if (f is BottomSheetDialogFragment) f.dialog?.window?.decorView else f?.view
            val marginBottom = if (f is BottomSheetDialogFragment) navBarHeight else 0
            makeSnackBar(pair.first, action = { pair.second }, root = root, marginBottom = marginBottom).show()
        })

    }

    override fun provideViewModel(): Map<KClass<*>, () -> BaseViewModel> = mapOf(
        vmCreator(MainViewModel::class)
    )

}