package kz.adamant.search.ui.type

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.parcelize.Parcelize
import kz.adamant.arch.models.OutcomeState
import kz.adamant.common.BaseViewModel
import kz.adamant.common.binding.BindingFragment
import kz.adamant.common.R
import kz.adamant.common.extensions.setUpShimmerVisibility
import kz.adamant.preferences.UserPreferencesAdapter
import kz.adamant.preferences.domain.Preferences
import kz.adamant.search.databinding.FragmentSearchByTypeBinding
import kotlin.reflect.KClass

class SearchByTypeFragment : BindingFragment<FragmentSearchByTypeBinding>(
    FragmentSearchByTypeBinding::inflate
) {
    private val args by navArgs<SearchByTypeFragmentArgs>()

    override fun provideViewModel(): Map<KClass<*>, () -> BaseViewModel> = mapOf(
        vmCreator(SearchByTypeViewModel::class)
    )

    private val vm get() = vm(SearchByTypeViewModel::class)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvTitle.text = getString(when(args.searchType) {
            SearchType.CUISINE -> R.string.preferences_cuisines
            SearchType.CATEGORY -> R.string.preferences_categories
        })
        binding.vgShimmer.setUpShimmerVisibility()
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        val adapter = UserPreferencesAdapter {
            vm.onClickType(it, args.searchType)
        }
        binding.rvCuisines.adapter = adapter
        vm.state.observe(viewLifecycleOwner) {
            when(it) {
                is OutcomeState.Success -> {
                    binding.vgShimmer.isVisible = false
                    val data = when(args.searchType) {
                        SearchType.CUISINE -> it.data?.cuisines
                        SearchType.CATEGORY -> it.data?.categories
                    } ?: listOf()
                    adapter.submitList(data)
                }
                else -> {
                    binding.vgShimmer.isVisible = true
                }
            }
        }

    }
}