package kz.adamant.search.ui.filter

import kz.adamant.common.BaseViewModel
import kz.adamant.search.domain.SearchFilter

class SearchFilterViewModel : BaseViewModel() {

    var data = SearchFilter()

    fun updateComplexity(pos: Int?) {
        data = data.copy(complexityId = pos)
    }

    fun updateRange(min: Int, max: Int) {
        data = if (min == MIN_RANGE && max == MAX_RANGE) data.copy(
            minCookingTime = null,
            maxCookingTime = null
        ) else data.copy(
            minCookingTime = min,
            maxCookingTime = max
        )
    }

    fun updateCalories(value: Int) {
        data = data.copy(
            calories = value.coerceAtLeast(1)
        )
    }

    companion object {
        const val MIN_RANGE = 0
        const val MAX_RANGE = 130
    }
}