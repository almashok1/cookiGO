package kz.adamant.search.ui.result

import kotlinx.coroutines.Dispatchers
import kz.adamant.arch.utils.asSuccess
import kz.adamant.arch.utils.map
import kz.adamant.common.BaseViewModel
import kz.adamant.common.extensions.getState
import kz.adamant.common.extensions.liveData
import kz.adamant.data.managers.SessionManager
import kz.adamant.recipe.RecipeNavigator
import kz.adamant.recipe.domain.RecipeInteractor
import kz.adamant.recipe.ui.models.RecipeDvo
import kz.adamant.recipe.ui.models.RecipeDvoToRecipeModelMapper
import kz.adamant.recipe.ui.models.RecipeToRecipeDvoMapper
import kz.adamant.search.domain.SearchFilter
import kz.adamant.search.domain.SearchInteractor

class SearchResultViewModel(
    private val recipeInteractor: RecipeInteractor,
    private val searchInteractor: SearchInteractor,
    private val recipeDvoMapper: RecipeToRecipeDvoMapper,
    private val recipeModelMapper: RecipeDvoToRecipeModelMapper,
    private val recipeNavigator: RecipeNavigator
) : BaseViewModel() {

    private val _recipe = getState<List<RecipeDvo>>(Dispatchers.IO, debounce = 800L)
    val recipe = _recipe.liveData

    var inFilter = SearchFilter()
        private set

    fun loadRecipes(request: SearchRequestDvo?) {
        loadRecipes(request?.modifySearchFilter(inFilter))
    }

    fun loadRecipes(filter: SearchFilter?) {
        inFilter = filter ?: inFilter
        _recipe.request {
            searchInteractor.requestSearch(inFilter).map {
                it?.recipes?.map(recipeDvoMapper::map)
            }
        }
    }

    fun search(text: String?) {
        loadRecipes(inFilter.copy(word = text?.ifEmpty { null }))
    }

    fun switchFavourite(recipe: RecipeDvo) {
        if (recipe.isFavourite) removeFavourite(recipe) else addFavourite(recipe)
    }

    fun openRecipeDetail(recipeDvo: RecipeDvo) = withFragment {
        recipeNavigator.openRecipeDetails(it, recipeDvo)
    }

    private fun addFavourite(recipe: RecipeDvo) = launchNotBoundIO {
        recipeInteractor.addFavourite(
            SessionManager.userId,
            recipeModelMapper.map(recipe)
        )
    }

    private fun removeFavourite(recipe: RecipeDvo) = launchNotBoundIO {
        recipeInteractor.removeFavourite(
            SessionManager.userId,
            recipeModelMapper.map(recipe)
        )
    }

}