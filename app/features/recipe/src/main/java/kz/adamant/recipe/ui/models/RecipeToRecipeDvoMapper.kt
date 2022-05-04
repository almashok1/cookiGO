package kz.adamant.recipe.ui.models

import kz.adamant.recipe.domain.RecipeModel

class RecipeToRecipeDvoMapper {
    fun map(input: RecipeModel, isFavourite: Boolean = false) = RecipeDvo(
        id = input.id,
        title = input.name ?: "",
        imageUrl = input.image ?: "",
        rate = input.avgStars?.toRate() ?: input.rate?.toRate() ?: 0,
        category = RecipeDvo.Category(
            input.category?.id ?: -1L,
            input.name ?: "",
            input.image ?: ""
        ),
        complexity = when (input.complexity?.name?.lowercase()) {
            "medium" -> RecipeDvo.Complexity.MEDIUM
            "hard" -> RecipeDvo.Complexity.HARD
            else -> RecipeDvo.Complexity.EASY
        },
        complexityId = input.complexity?.id ?: -1,
        description = input.description ?: "",
        avgComments = input.amountOfComment ?: 0L,
        timeToCook = input.cookingTime ?: 0,
        calorie = input.calorie ?: 0,
        isFavourite = if (input.isFavourite != null) input.isFavourite == 1 else isFavourite
    )

    private fun Double?.toRate() = (this?.toInt() ?: 0).coerceIn(0..5)
}

class RecipeDvoToRecipeModelMapper {
    fun map(input: RecipeDvo) = RecipeModel(
        id = input.id,
        name = input.title,
        image = input.imageUrl,
        avgStars = input.rate.toDouble(),
        rate = input.rate.toDouble(),
        category = RecipeModel.Category(
            input.category.id,
            input.category.name,
            input.category.image
        ),
        complexity = RecipeModel.Complexity(
            input.complexityId,
            input.complexity.name.lowercase()
        ),
        description = input.description,
        amountOfComment = input.avgComments,
        cookingTime = input.timeToCook,
        calorie = input.calorie,
        isFavourite = if (input.isFavourite) 1 else 0
    )
}