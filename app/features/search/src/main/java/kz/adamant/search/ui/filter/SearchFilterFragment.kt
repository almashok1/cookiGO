package kz.adamant.search.ui.filter

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import kz.adamant.common.BaseViewModel
import kz.adamant.common.R
import kz.adamant.common.binding.BindingBottomSheetFragment
import kz.adamant.common.extensions.observeSavedState
import kz.adamant.common.extensions.onSafeClick
import kz.adamant.common.extensions.setToPrevSavedState
import kz.adamant.common.extensions.setUpRoundedCorners
import kz.adamant.search.databinding.FragmentSearchFilterBinding
import kz.adamant.search.domain.SearchFilter
import kz.adamant.search.ui.result.SearchRequestDvo
import kotlin.reflect.KClass

class SearchFilterFragment : BindingBottomSheetFragment<FragmentSearchFilterBinding>(
    FragmentSearchFilterBinding::inflate
) {
    override fun provideViewModel(): Map<KClass<*>, () -> BaseViewModel> = mapOf(
        vmCreator(SearchFilterViewModel::class)
    )

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

    private val vm get() = vm(SearchFilterViewModel::class)!!

    private val args by navArgs<SearchFilterFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (dialog as BottomSheetDialog).setUpRoundedCorners()
        vm.data = args.filter

        setFilter(vm.data)
        binding.slider.setLabelFormatter {
            when (it) {
                binding.slider.valueTo -> getString(R.string.common_cookingTime_moreThan2h)
                binding.slider.valueFrom -> getString(R.string.common_cookingTime_upTo10min)
                else -> getString(R.string.common_cookingTimeEndLine, it.toInt())
            }
        }

        binding.btnApply.onSafeClick {
            vm.updateComplexity(
                when (binding.chipGroup.checkedChipId) {
                    kz.adamant.search.R.id.chipEasy -> 1
                    kz.adamant.search.R.id.chipMedium -> 2
                    kz.adamant.search.R.id.chipHard -> 3
                    else -> null
                }
            )
            vm.updateRange(binding.slider.values[0].toInt(), binding.slider.values[1].toInt())
            vm.updateCalories(binding.sliderCalories.value.toInt())
            setToPrevSavedState(KEY_OBSERVE, vm.data)
            findNavController().navigateUp()
        }
        binding.tvReset.onSafeClick {
            vm.data = args.request.getDefaultFilter()
            setToPrevSavedState(KEY_OBSERVE, vm.data)
            findNavController().navigateUp()
        }
    }

    private fun setFilter(filter: SearchFilter) = with(binding) {
        val calories = (filter.calories ?: 1).coerceAtLeast(0)
        binding.sliderCalories.setValue(calories.toFloat())
        when(filter.complexityId) {
            1 -> chipGroup.check(kz.adamant.search.R.id.chipEasy)
            2 -> chipGroup.check(kz.adamant.search.R.id.chipMedium)
            3 -> chipGroup.check(kz.adamant.search.R.id.chipHard)
            else -> chipGroup.clearCheck()
        }
        val min = vm.data.minCookingTime
        val max = vm.data.maxCookingTime
        if (min != null && max != null) binding.slider.setValues(min.toFloat(), max.toFloat())
    }

    companion object {
        private const val ARG_FILTER = "filter"
        private const val ARG_REQUEST = "request"
        private const val KEY_OBSERVE = "filter_key"
        fun open(
            fragment: Fragment,
            filter: SearchFilter,
            request: SearchRequestDvo,
            @IdRes id: Int = kz.adamant.search.R.id.action_global_searchFilterFragment,
        ) {
            fragment.findNavController().navigate(
                id,
                bundleOf(ARG_FILTER to filter, ARG_REQUEST to request)
            )
        }

        fun observe(
            fragment: Fragment,
            onApplyFilter: (SearchFilter) -> Unit
        ) {
            fragment.observeSavedState(KEY_OBSERVE, handle = onApplyFilter)
        }
    }
}