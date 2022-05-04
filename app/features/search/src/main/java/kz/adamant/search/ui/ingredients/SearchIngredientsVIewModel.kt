package kz.adamant.search.ui.ingredients

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import kz.adamant.checklist.domain.ChecklistInteractor
import kz.adamant.checklist.ui.ChecklistAbstractViewModel
import kz.adamant.common.extensions.navigateWithAnim
import kz.adamant.search.R
import kz.adamant.search.ui.SearchFragmentDirections
import kz.adamant.search.ui.result.SearchRequestDvo

class SearchIngredientsVIewModel(
    private val checklistInteractor: ChecklistInteractor
) : ChecklistAbstractViewModel(checklistInteractor) {

    val checklists = checklistInteractor.flow.asLiveData(viewModelScope.coroutineContext)

    fun openResult() = withFragment {
        it.findNavController().navigateWithAnim(
            SearchFragmentDirections.actionGlobalSearchResultFragment(
                SearchRequestDvo.FromIngredients(
                    checklists.value?.map { item -> item.name } ?: listOf()
                )
            )
        )
    }

    override fun onCleared() {
        checklistInteractor.clear()
        super.onCleared()
    }
}