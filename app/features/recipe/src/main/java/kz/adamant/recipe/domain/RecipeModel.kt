package kz.adamant.recipe.domain

data class RecipeModel(
    val id: Long,
    val name: String? = null,
    val avgStars: Double? = null,
    val rate: Double? = null,
    val image: String? = null,
    val complexity: Complexity? = null,
    val cookingTime: Int? = null,
    val calorie: Int? = null,
    val category: Category? = null,
    val description: String? = null,
    val amountOfComment: Long? = null,
    val isFavourite: Int? = null
) {

    class Complexity(
        val id: Long,
        val name: String? = null
    )
    class Category(
        val id: Long,
        val name: String? = null,
        val image: String? = null
    )
}