package kz.adamant.search.ui.type

import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay
import kz.adamant.common.BaseViewModel
import kz.adamant.common.extensions.getState
import kz.adamant.common.extensions.liveData
import kz.adamant.common.extensions.navigateWithAnim
import kz.adamant.preferences.domain.Preferences
import kz.adamant.preferences.domain.UserPreferencesInteractor
import kz.adamant.search.R
import kz.adamant.search.ui.result.SearchRequestDvo
import kz.adamant.search.ui.result.SearchResultFragmentDirections

class SearchByTypeViewModel(
    private val userPreferencesInteractor: UserPreferencesInteractor
) : BaseViewModel() {

    private val _state = getState<Preferences>()
    val state = _state.liveData

    init {
        loadPreferences()
    }

    private fun loadPreferences() {
        launchIO {
            _state.request(100L, userPreferencesInteractor::getPreferences)
        }
    }

    fun onClickType(item: Preferences.Item, type: SearchType) = withFragment {
        it.findNavController().navigateWithAnim(
            SearchResultFragmentDirections.actionGlobalSearchResultFragment(
                when(type) {
                    SearchType.CUISINE -> SearchRequestDvo.FromCuisine(item.id)
                    SearchType.CATEGORY -> SearchRequestDvo.FromCategory(item.id)
                }
            )
        )
    }

}