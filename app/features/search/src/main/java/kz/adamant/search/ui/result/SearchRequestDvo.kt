package kz.adamant.search.ui.result

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kz.adamant.checklist.domain.Checklist
import kz.adamant.search.domain.SearchFilter

sealed class SearchRequestDvo : Parcelable {

    abstract fun modifySearchFilter(searchFilter: SearchFilter): SearchFilter
    abstract fun getDefaultFilter(): SearchFilter

    @Parcelize
    class FromCategory(val id: Long) : SearchRequestDvo() {
        override fun modifySearchFilter(searchFilter: SearchFilter): SearchFilter {
            return searchFilter.copy(categoryId = id)
        }

        override fun getDefaultFilter() = SearchFilter(categoryId = id)
    }

    @Parcelize
    class FromCuisine(val id: Long) : SearchRequestDvo() {
        override fun modifySearchFilter(searchFilter: SearchFilter): SearchFilter {
            return searchFilter.copy(cuisineId = id)
        }

        override fun getDefaultFilter() = SearchFilter(categoryId = id)
    }

    @Parcelize
    class FromIngredients(val ingredients: List<String>) : SearchRequestDvo() {
        override fun modifySearchFilter(searchFilter: SearchFilter): SearchFilter {
            return searchFilter.copy(ingredientList = ingredients)
        }

        override fun getDefaultFilter() = SearchFilter()
    }

    @Parcelize
    object All : SearchRequestDvo() {
        override fun modifySearchFilter(searchFilter: SearchFilter): SearchFilter {
            return searchFilter
        }

        override fun getDefaultFilter() = SearchFilter()
    }
}