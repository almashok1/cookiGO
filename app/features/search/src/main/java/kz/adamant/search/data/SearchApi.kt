package kz.adamant.search.data

import kz.adamant.arch.models.Outcome
import kz.adamant.recipe.domain.RecipeModel
import kz.adamant.search.domain.SearchFilter
import retrofit2.http.Body
import retrofit2.http.POST

interface SearchApi {

    @POST("api/search")
    suspend fun getSearchResult(
        @Body request: SearchFilter
    ): Outcome<SearchResponse>

}

data class SearchResponse(
    val recipes: List<RecipeModel>
)