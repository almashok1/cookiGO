package kz.adamant.recipe.ui.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kz.adamant.arch.utils.doOnError
import kz.adamant.common.BaseViewModel
import kz.adamant.common.MainNavControllerFinder
import kz.adamant.common.REQUEST_UPDATE
import kz.adamant.common.extensions.setMain
import kz.adamant.common.extensions.setToCurrentSavedState
import kz.adamant.common.extensions.setToPrevSavedState
import kz.adamant.data.managers.SessionManager
import kz.adamant.recipe.domain.RecipeInteractor
import kz.adamant.recipe.ui.models.RecipeDvo
import kz.adamant.recipe.ui.models.RecipeDvoToRecipeModelMapper

class RecipeDetailViewModel(
    private val recipeInteractor: RecipeInteractor,
    private val recipeModelMapper: RecipeDvoToRecipeModelMapper,
    private val mainNavControllerFinder: MainNavControllerFinder
) : BaseViewModel() {

    private val _portions = MutableLiveData(1)
    val portions: LiveData<Int> get() = _portions

    private val _recipe = MutableLiveData<RecipeDvo>()
    val recipe: LiveData<RecipeDvo> get() = _recipe

    fun setRecipe(recipe: RecipeDvo) {
        _recipe.value = recipe
    }

    fun updatePortions(size: Int) {
        if (size <= 0) return
        _portions.value = size
    }

    private fun addFavourite() = launchNotBoundIO {
        val inRecipe = _recipe.value ?: return@launchNotBoundIO
        _recipe.setMain(inRecipe.copy(isFavourite = true))
        recipeInteractor.addFavourite(
            SessionManager.userId,
            recipeModelMapper.map(inRecipe)
        ).doOnError {
            _recipe.setMain(inRecipe.copy(isFavourite = inRecipe.isFavourite))
        }.handleError()
    }

    private fun removeFavourite() = launchNotBoundIO {
        val inRecipe = _recipe.value ?: return@launchNotBoundIO
        _recipe.setMain(inRecipe.copy(isFavourite = false))
        recipeInteractor.removeFavourite(
            SessionManager.userId,
            recipeModelMapper.map(inRecipe)
        ).doOnError {
            _recipe.setMain(inRecipe.copy(isFavourite = inRecipe.isFavourite))
        }.handleError()
    }

    fun toggleFavourite() = launchNotBoundIO {
        val recipe = _recipe.value ?: return@launchNotBoundIO
        if (recipe.isFavourite) removeFavourite() else addFavourite()
    }
}
