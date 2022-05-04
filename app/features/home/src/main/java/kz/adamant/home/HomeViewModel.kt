package kz.adamant.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kz.adamant.arch.utils.doOnError
import kz.adamant.arch.utils.doOnSuccess
import kz.adamant.arch.utils.map
import kz.adamant.common.BaseViewModel
import kz.adamant.common.MainNavControllerFinder
import kz.adamant.common.extensions.setMain
import kz.adamant.data.managers.SessionManager
import kz.adamant.recipe.RecipeNavigator
import kz.adamant.recipe.domain.RecipeInteractor
import kz.adamant.recipe.ui.models.RecipeDvo
import kz.adamant.recipe.ui.models.RecipeDvoToRecipeModelMapper
import kz.adamant.recipe.ui.models.RecipeToRecipeDvoMapper
import kz.adamant.recipe.ui.models.RecommendationDvo

class HomeViewModel(
    private val recipeInteractor: RecipeInteractor,
    private val recipeDvoMapper: RecipeToRecipeDvoMapper,
    private val recipeModelMapper: RecipeDvoToRecipeModelMapper,
    private val recipeNavigator: RecipeNavigator,
    private val mainNavControllerFinder: MainNavControllerFinder
) : BaseViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _recommendations = MutableLiveData<RecommendationDvo>()
    val recommendations: LiveData<RecommendationDvo> = _recommendations

    fun getRecommendations(withLoading: Boolean = true) = launchIO {
        if (withLoading) _loading.setMain(true)
        recipeInteractor.getRecommendationList(SessionManager.userId)
            .map {
                RecommendationDvo(
                    it?.dailyRecommendation?.let { recipe ->
                        recipeDvoMapper.map(recipe)
                    },
                    recommendations = (it?.recommendations ?: listOf()).map { recipe ->
                        recipeDvoMapper.map(recipe)
                    }
                )
            }
            .doOnSuccess {
                withContext(Dispatchers.Main) {
                    it?.let(_recommendations::setValue)
                    _loading.value = false
                }
            }
            .handleError()
    }.bindSubscribe("getRecommendations")

    fun handleFavouriteAction(recipe: RecipeDvo) {
        if (recipe.isFavourite) removeFavourite(recipe) else addFavourite(recipe)
    }

    private fun addFavourite(recipe: RecipeDvo) = launchNotBoundIO {
        updateRecommendations(recipe.copy(isFavourite = true))
        recipeInteractor.addFavourite(
            SessionManager.userId,
            recipeModelMapper.map(recipe)
        ).doOnError {
            updateRecommendations(recipe)
        }
    }

    private fun removeFavourite(recipe: RecipeDvo) = launchNotBoundIO {
        updateRecommendations(recipe.copy(isFavourite = false))
        recipeInteractor.removeFavourite(
            SessionManager.userId,
            recipeModelMapper.map(recipe)
        ).doOnError {
            updateRecommendations(recipe)
        }
    }

    fun openRecipeDetail(recipeDvo: RecipeDvo) = withFragment {
        recipeNavigator.openRecipeDetails(it, recipeDvo)
    }

    private fun updateRecommendations(recipe: RecipeDvo) = launchIO {
        val recommendations = _recommendations.value ?: return@launchIO
        val check = { current: RecipeDvo ->
            recipe.id == current.id
        }
        var daily = recommendations.daily
        if (daily != null && check(daily)) {
            daily = recipe
        }
        val all = recommendations.recommendations.map {
            if (check(it)) recipe else it
        }
        _recommendations.setMain(
            recommendations.copy(
                daily = daily,
                recommendations = all
            )
        )
    }

}