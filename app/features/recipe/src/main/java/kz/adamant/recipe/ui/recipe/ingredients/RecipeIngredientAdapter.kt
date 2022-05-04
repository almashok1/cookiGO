package kz.adamant.recipe.ui.recipe.ingredients

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import kz.adamant.common.binding.BindingBasicAsyncRvAdapter
import kz.adamant.common.extensions.getIngredientDrawableFromPosition
import kz.adamant.common.extensions.onSafeClick
import kz.adamant.recipe.databinding.ItemRecipeIngredientBinding
import kz.adamant.recipe.domain.IngredientRef

class RecipeIngredientAdapter(
    private val onAddClick: (IngredientRef, Int) -> Unit
) : BindingBasicAsyncRvAdapter<IngredientRef, ItemRecipeIngredientBinding>(
    Callback(), ItemRecipeIngredientBinding::inflate
) {

    class Callback : DiffUtil.ItemCallback<IngredientRef>() {
        override fun areItemsTheSame(oldItem: IngredientRef, newItem: IngredientRef): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: IngredientRef, newItem: IngredientRef): Boolean {
            return oldItem == newItem
        }
    }

    @SuppressLint("SetTextI18n")
    override fun bind(item: IngredientRef, binding: ItemRecipeIngredientBinding, position: Int) =
        with(binding) {
            super.bind(item, binding, position)
            ivAdd.isEnabled = item.isEnabled
            tvName.text = item.ingredient.name
            tvSize.text = "${item.quantity} ${item.ingredient.unitOfMeasurement?.name ?: "pcs"}"
            ivAdd.onSafeClick { onAddClick(item, position) }
            root.setBackgroundResource(getIngredientDrawableFromPosition(position, itemCount))
        }
}