package kz.adamant.recipe.ui.models

sealed class FavouriteAction {
    data class Remove(
        val recipeDvo: RecipeDvo
    ): FavouriteAction()

    data class Add(
        val recipeDvo: RecipeDvo
    ): FavouriteAction()

    object None: FavouriteAction()
}