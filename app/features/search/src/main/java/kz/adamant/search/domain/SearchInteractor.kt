package kz.adamant.search.domain

import kz.adamant.search.data.SearchApi

class SearchInteractor(
    private val api: SearchApi
) {

    suspend fun requestSearch(request: SearchFilter) = api.getSearchResult(request)

}