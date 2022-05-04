package kz.adamant.search.ui.ingredients

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import kz.adamant.common.BaseViewModel
import kz.adamant.common.binding.BindingFragment
import kz.adamant.common.extensions.beginChangeBoundsTransition
import kz.adamant.common.extensions.onSafeClick
import kz.adamant.search.databinding.FragmentSearchIngredientsBinding
import kotlin.reflect.KClass

class SearchIngredientsFragment : BindingFragment<FragmentSearchIngredientsBinding>(
    FragmentSearchIngredientsBinding::inflate
) {
    override fun provideViewModel(): Map<KClass<*>, () -> BaseViewModel> = mapOf(
        vmCreator(SearchIngredientsVIewModel::class)
    )

    private val vm get() = vm(SearchIngredientsVIewModel::class)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setChecklistView()
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        vm.checklists.observe(viewLifecycleOwner) {
            binding.vChecklist.submitChecklists(it)
            binding.btnSearch.isEnabled = it.isNotEmpty()
            binding.vChecklist.beginChangeBoundsTransition()
        }
        binding.btnSearch.onSafeClick {
            vm.openResult()
        }
    }

    private fun setChecklistView() = with(binding.vChecklist) {
        this.vm = { this@SearchIngredientsFragment.vm }
    }
}