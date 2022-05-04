package kz.adamant.search.ui

import androidx.navigation.fragment.findNavController
import kz.adamant.common.BaseViewModel
import kz.adamant.common.extensions.navigateWithAnim
import kz.adamant.search.R
import kz.adamant.search.ui.result.SearchRequestDvo
import kz.adamant.search.ui.result.SearchResultFragmentDirections
import kz.adamant.search.ui.type.SearchType

class SearchViewModel : BaseViewModel() {


    private fun openByType(type: SearchType) = withFragment {
        it.findNavController().navigateWithAnim(
            SearchFragmentDirections.actionSearchFragmentToSearchByTypeFragment(type)
        )
    }

    fun openByCuisines()  {
        openByType(SearchType.CUISINE)
    }

    fun openByCategory()  {
        openByType(SearchType.CATEGORY)
    }

    fun openByIngredients() = withFragment {
        it.findNavController().navigateWithAnim(
            R.id.action_global_searchIngredientsFragment
        )
    }

    fun openSearchResult() = withFragment {
        it.findNavController().navigateWithAnim(
            SearchResultFragmentDirections.actionGlobalSearchResultFragment(SearchRequestDvo.All, true)
        )
    }
}
