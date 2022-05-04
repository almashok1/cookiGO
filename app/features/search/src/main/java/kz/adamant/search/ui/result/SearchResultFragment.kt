package kz.adamant.search.ui.result

import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kz.adamant.arch.models.OutcomeState
import kz.adamant.common.BaseViewModel
import kz.adamant.common.KeyboardUtils
import kz.adamant.common.binding.BindingFragment
import kz.adamant.common.extensions.setUpShimmerVisibility
import kz.adamant.recipe.ui.adapters.RecipeMediumAdapter
import kz.adamant.search.R
import kz.adamant.search.databinding.FragmentSearchResultBinding
import kz.adamant.search.ui.filter.SearchFilterFragment
import kotlin.reflect.KClass

class SearchResultFragment : BindingFragment<FragmentSearchResultBinding>(
    FragmentSearchResultBinding::inflate
) {
    override fun provideViewModel(): Map<KClass<*>, () -> BaseViewModel> = mapOf(
        vmCreator(SearchResultViewModel::class)
    )

    private val vm get() = vm(SearchResultViewModel::class)!!

    private val args by navArgs<SearchResultFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vgShimmer.setUpShimmerVisibility()
        binding.etSearch.requestFocus()
        if (args.shouldFirstOpenSearch) KeyboardUtils.show(binding.etSearch)
        val adapter = RecipeMediumAdapter(
            onClick = vm::openRecipeDetail,
            onFavouriteClick = vm::switchFavourite
        )
        binding.rv.adapter = adapter
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        vm.loadRecipes(args.request)
        binding.tilSearch.setEndIconOnClickListener {
            SearchFilterFragment.open(this, vm.inFilter, args.request)
        }
        binding.etSearch.doAfterTextChanged {
            vm.search(it?.toString())
        }
        SearchFilterFragment.observe(this) {
            vm.loadRecipes(it)
        }
        vm.recipe.observe(viewLifecycleOwner) {
            when (it) {
                is OutcomeState.Success -> {
                    binding.vgShimmer.isInvisible = true
                    val data = it.data ?: listOf()
                    val isEmpty = data.isEmpty()
                    binding.vEmpty.isVisible = isEmpty
                    binding.rv.isVisible = !isEmpty
                    adapter.submitList(data)
                }
                else -> {
                    binding.vgShimmer.isVisible = true
                    binding.vEmpty.isVisible = false
                    binding.rv.isVisible = false
                }
            }
        }
    }
}