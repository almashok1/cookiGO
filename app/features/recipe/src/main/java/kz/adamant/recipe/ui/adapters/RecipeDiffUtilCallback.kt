package kz.adamant.recipe.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import kz.adamant.recipe.ui.models.RecipeDvo

class RecipeDiffUtilCallback : DiffUtil.ItemCallback<RecipeDvo>() {
    override fun areItemsTheSame(oldItem: RecipeDvo, newItem: RecipeDvo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RecipeDvo, newItem: RecipeDvo): Boolean {
        return oldItem == newItem
    }
}