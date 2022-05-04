package kz.adamant.recipe.domain

data class IngredientListDto(
    val ingredientList: List<IngredientRefDto>
)

data class IngredientList(
    val ingredientList: List<IngredientRef>
)

fun IngredientListDto.toModel() = IngredientList(
    ingredientList = this.ingredientList.map {
        IngredientRef(it.id, it.ingredient, it.recipeId, it.quantity ?: 0, it.quantity ?: 0)
    }
)

data class IngredientRefDto(
    val id: Long,
    val ingredient: Ingredient,
    val recipeId: Long,
    val quantity: Long?
)

data class IngredientRef(
    val id: Long,
    val ingredient: Ingredient,
    val recipeId: Long,
    val quantity: Long,
    val initialQuantity: Long,
    val isEnabled: Boolean = true
)

data class Ingredient(
    val id: Long,
    val name: String?,
    val unitOfMeasurement: UnitOfMeasurement?,
) {
    class UnitOfMeasurement(
        val id: Long,
        val name: String
    )
}