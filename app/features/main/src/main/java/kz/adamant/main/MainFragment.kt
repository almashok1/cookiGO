package kz.adamant.main

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import kz.adamant.common.BaseViewModel
import kz.adamant.common.binding.BindingFragment
import kz.adamant.common.extensions.onSafeClick
import kz.adamant.main.databinding.FragmentMainBinding
import kotlin.reflect.KClass

class MainFragment : BindingFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {
    override fun provideViewModel(): Map<KClass<*>, () -> BaseViewModel> = mapOf(
        vmCreator(MainFeatureViewModel::class)
    )

    private val vm get() = vm(MainFeatureViewModel::class)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment = childFragmentManager
            .findFragmentById(R.id.mainScreenNavHostFragment) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHostFragment.navController)
        binding.bottomNavigationView.menu.findItem(R.id.placeholder).isEnabled = false
        binding.fab.onSafeClick {
            vm.openSearch()
        }
    }
}
