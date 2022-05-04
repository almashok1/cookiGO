package kz.adamant.recipe.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kz.adamant.recipe.domain.RecipeModel

class RecipeLocalDataSource {

    private val _favourites = MutableStateFlow<List<RecipeModel>>(listOf())

    val favourites get(): Flow<List<RecipeModel>> = _favourites

    fun getCurrentFavourites() = _favourites.value

    fun updateFavourites(
        list: List<RecipeModel>
    ) {
        _favourites.value = list
    }

    fun addFavourite(recipe: RecipeModel) {
        _favourites.value = _favourites.value + recipe
    }

    fun removeFavourite(recipe: RecipeModel) {
        _favourites.value = _favourites.value.filterNot {
            it.id == recipe.id
        }
    }
}