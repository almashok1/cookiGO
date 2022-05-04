package kz.adamant.search.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import kz.adamant.common.BaseViewModel
import kz.adamant.common.binding.BindingFragment
import kz.adamant.common.extensions.onSafeClick
import kz.adamant.search.R
import kz.adamant.search.databinding.FragmentSearchBinding
import kotlin.reflect.KClass

class SearchFragment : BindingFragment<FragmentSearchBinding>(
    FragmentSearchBinding::inflate
) {
    override fun provideViewModel(): Map<KClass<*>, () -> BaseViewModel> = mapOf(
        vmCreator(SearchViewModel::class)
    )

    private val vm get() = vm(SearchViewModel::class)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickers()
    }

    private fun setupClickers() = with(binding) {
        vgCategory.onSafeClick { vm.openByCategory() }
        vgCuisine.onSafeClick { vm.openByCuisines() }
        vgIngredients.onSafeClick { vm.openByIngredients() }
        etSearch.onSafeClick {
            vm.openSearchResult()
        }
    }
}