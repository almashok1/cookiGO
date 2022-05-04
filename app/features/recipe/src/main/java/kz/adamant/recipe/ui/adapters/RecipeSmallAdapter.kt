package kz.adamant.recipe.ui.adapters

import kz.adamant.common.binding.BindingBasicAsyncRvAdapter
import kz.adamant.common.extensions.getColorCompat
import kz.adamant.common.extensions.loadWithDriveUrl
import kz.adamant.common.extensions.onSafeClick
import kz.adamant.recipe.databinding.ViewDailyRecommendationBinding
import kz.adamant.recipe.ui.models.RecipeDvo

class RecipeSmallAdapter(
    private val onClick: (RecipeDvo) -> Unit,
    private val onFavouriteClick: (RecipeDvo) -> Unit
) : BindingBasicAsyncRvAdapter<RecipeDvo, ViewDailyRecommendationBinding>(
    RecipeDiffUtilCallback(), ViewDailyRecommendationBinding::inflate
) {

    override fun bind(item: RecipeDvo, binding: ViewDailyRecommendationBinding, position: Int) =
        with(binding) {
            bindRecipeSmall(item, onClick, onFavouriteClick)
        }
}

fun ViewDailyRecommendationBinding.bindRecipeSmall(
    dvo: RecipeDvo,
    onClick: (RecipeDvo) -> Unit,
    onFavouriteClick: (RecipeDvo) -> Unit,
) {
    tvTitle.text = dvo.title
    ivPhoto.loadWithDriveUrl(dvo.imageUrl)
    vStarRate.setRate(dvo.rate)
    tvCategory.text = dvo.category.name
    cvComplexity.setCardBackgroundColor(root.context.getColorCompat(dvo.complexity.background))
    tvComplexity.text = root.context.getString(dvo.complexity.title)
    tvCalorie.text =
        root.context.getString(kz.adamant.common.R.string.common_calorie, dvo.calorie.toString())
    tvTimeToCook.text =
        root.context.getString(
            kz.adamant.common.R.string.common_cookingTime,
            dvo.timeToCook.toString()
        )
    ivFavourite.isActivated = dvo.isFavourite
    ivFavourite.onSafeClick {
        ivFavourite.isActivated = !ivFavourite.isActivated
        onFavouriteClick(dvo)
    }
    root.onSafeClick {
        onClick(dvo)
    }
}