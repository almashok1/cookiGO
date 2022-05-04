package kz.adamant.preferences

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import kz.adamant.arch.models.OutcomeState
import kz.adamant.common.BaseViewModel
import kz.adamant.common.binding.BindingFragment
import kz.adamant.common.extensions.onSafeClick
import kz.adamant.common.extensions.setShowProgress
import kz.adamant.common.extensions.setUpShimmerVisibility
import kz.adamant.preferences.databinding.FragmentUserPreferencesBinding
import kz.adamant.preferences.domain.Preferences
import kotlin.reflect.KClass

class UserPreferencesFragment : BindingFragment<FragmentUserPreferencesBinding>(
    FragmentUserPreferencesBinding::inflate
) {
    private val cuisinesAdapter = UserPreferencesAdapter { checkButtonEnabled() }
    private val categoriesAdapter = UserPreferencesAdapter { checkButtonEnabled() }

    companion object {
        private const val MIN_PREFERENCES_AMOUNT = 3
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            shimmer.setUpShimmerVisibility()
            layoutMain.rvCuisines.adapter = cuisinesAdapter
            layoutMain.rvCategories.adapter = categoriesAdapter
            vm(UserPreferencesViewModel::class)?.run {
                state.observe(viewLifecycleOwner) {
                    when (it) {
                        is OutcomeState.Loading -> {
                            layoutMain.root.isVisible = false
                            shimmer.isVisible = true
                        }
                        is OutcomeState.Success -> {
                            layoutMain.root.isVisible = true
                            shimmer.isVisible = false
                            cuisinesAdapter.submitList(it.data?.cuisines ?: listOf())
                            categoriesAdapter.submitList(it.data?.categories ?: listOf())
                        }
                        else -> {}
                    }
                }
                loadingSubmit.observe(viewLifecycleOwner) {
                    btnStart.setShowProgress(it, getString(kz.adamant.common.R.string.common_lets_start))
                    if (!it) checkButtonEnabled() else btnStart.isEnabled = false
                }
                btnStart.onSafeClick {
                    submit(
                        cuisines = cuisinesAdapter.getSelectedItems(),
                        categories = categoriesAdapter.getSelectedItems()
                    )
                }
            }
        }
    }

    private fun checkButtonEnabled() {
        val size = categoriesAdapter.getSelectedItems().size + cuisinesAdapter.getSelectedItems().size
        binding.btnStart.isEnabled = size >= MIN_PREFERENCES_AMOUNT
    }

    override fun provideViewModel(): Map<KClass<*>, () -> BaseViewModel> = mapOf(
        vmCreator(UserPreferencesViewModel::class)
    )
}