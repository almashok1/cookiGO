package kz.adamant.recipe.ui.recipe.step

import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import kz.adamant.common.binding.BindingBasicAsyncRvAdapter
import kz.adamant.common.extensions.onSafeClick
import kz.adamant.common.R
import kz.adamant.common.extensions.loadWithDriveUrl
import kz.adamant.recipe.databinding.ItemStepByStepBinding
import kz.adamant.recipe.domain.CookingStep

class RecipeStepsAdapter(
    private val onClick: (CookingStep) -> Unit
) : BindingBasicAsyncRvAdapter<CookingStep, ItemStepByStepBinding>(
    Callback(), ItemStepByStepBinding::inflate
) {

    class Callback: DiffUtil.ItemCallback<CookingStep>() {
        override fun areItemsTheSame(oldItem: CookingStep, newItem: CookingStep): Boolean {
            return oldItem.stepNumber == newItem.stepNumber
        }

        override fun areContentsTheSame(oldItem: CookingStep, newItem: CookingStep): Boolean {
            return oldItem == newItem
        }
    }

    override fun bind(item: CookingStep, binding: ItemStepByStepBinding, position: Int) = with(binding) {
        super.bind(item, binding, position)
        tvStepNum.text = item.stepNumber.toString()
        tvDescription.text = item.text
        tvTitle.text = root.context.getString(R.string.recipe_stepNumber, item.stepNumber)
        vgTimer.isVisible = (item.duration ?: 0) > 0
        ivImage.loadWithDriveUrl(item.image)
        root.onSafeClick { onClick(item) }
    }

}