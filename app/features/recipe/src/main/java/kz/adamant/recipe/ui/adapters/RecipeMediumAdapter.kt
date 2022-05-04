package kz.adamant.recipe.ui.adapters

import android.annotation.SuppressLint
import kz.adamant.common.binding.BindingBasicAsyncRvAdapter
import kz.adamant.common.extensions.getColorCompat
import kz.adamant.common.extensions.loadWithDriveUrl
import kz.adamant.common.extensions.onSafeClick
import kz.adamant.recipe.databinding.ItemRecipeMediumBinding
import kz.adamant.recipe.ui.models.RecipeDvo

class RecipeMediumAdapter(
    private val onClick: (RecipeDvo) -> Unit,
    private val onFavouriteClick: (RecipeDvo) -> Unit
) : BindingBasicAsyncRvAdapter<RecipeDvo, ItemRecipeMediumBinding>(
    RecipeDiffUtilCallback(), ItemRecipeMediumBinding::inflate
) {

    override fun bind(item: RecipeDvo, binding: ItemRecipeMediumBinding, position: Int) =
        with(binding) {
            bind(item, onFavouriteClick, onClick)
        }
}

@SuppressLint("SetTextI18n")
fun ItemRecipeMediumBinding.bind(
    item: RecipeDvo,
    onClickFavourite: (RecipeDvo) -> Unit,
    onClick: (RecipeDvo) -> Unit
) {
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