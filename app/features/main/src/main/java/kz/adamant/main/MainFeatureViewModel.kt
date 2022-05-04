package kz.adamant.main

import kz.adamant.common.BaseViewModel
import kz.adamant.search.SearchNavigator

class MainFeatureViewModel(
    private val searchNavigator: SearchNavigator
) : BaseViewModel() {

    fun openSearch() = withFragment(searchNavigator::openSearch)

}