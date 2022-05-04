package kz.adamant.recipe.ui.adapters

import kz.adamant.common.binding.BindingBasicAsyncRvAdapter
import kz.adamant.common.extensions.getColorCompat
import kz.adamant.common.extensions.loadWithDriveUrl
import kz.adamant.common.extensions.onSafeClick
import kz.adamant.recipe.ui.models.RecipeDvo
import kz.adamant.recipe.databinding.ItemRecipeFullBinding

class RecipeFullAdapter(
    private val onClick: (RecipeDvo) -> Unit,
    private val onClickFavourite: (RecipeDvo) -> Unit
) : BindingBasicAsyncRvAdapter<RecipeDvo, ItemRecipeFullBinding>(
    RecipeDiffUtilCallback(), ItemRecipeFullBinding::inflate
) {
    override fun bind(item: RecipeDvo, binding: ItemRecipeFullBinding, position: Int) = with(binding) {
        tvTitle.text = item.title
        ivPhoto.loadWithDriveUrl(item.imageUrl)
        tvRate.text = item.rate.toString()
        cvComplexity.setCardBackgroundColor(root.context.getColorCompat(item.complexity.background))
        tvComplexity.text = root.context.getString(item.complexity.title)
        tvCommentsCount.text = "(${item.avgComments})"
        tvCalorie.text = root.context.getString(kz.adamant.common.R.string.common_calorie, item.calorie.toString())
        tvTimeToCook.text = root.context.getString(kz.adamant.common.R.string.common_cookingTime, item.timeToCook.toString())
        ivFavourite.isActivated = item.isFavourite
        ivFavourite.onSafeClick {
            onClickFavourite(item)
            ivFavourite.isActivated = !ivFavourite.isActivated
        }
        root.onSafeClick { onClick(item) }
    }
}