package kz.adamant.recipe.ui.models

data class RecommendationDvo(
    val daily: RecipeDvo?,
    val recommendations: List<RecipeDvo>
)