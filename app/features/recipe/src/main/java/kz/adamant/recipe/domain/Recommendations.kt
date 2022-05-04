package kz.adamant.recipe.domain

class Recommendations(
    val dailyRecommendation: RecipeModel,
    val recommendations: List<RecipeModel>
)

class RecommendationsModel(
    val dailyRecommendation: RecipeModel?,
    val recommendations: List<RecipeModel>
)
