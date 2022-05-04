package kz.adamant.recipe.ui.recipe.review

import androidx.recyclerview.widget.DiffUtil
import kz.adamant.common.binding.BindingBasicAsyncRvAdapter
import kz.adamant.common.extensions.toReadableFullFormat
import kz.adamant.recipe.databinding.ItemRecipeReviewBinding
import kz.adamant.recipe.domain.Review

class RecipeReviewAdapter : BindingBasicAsyncRvAdapter<Review, ItemRecipeReviewBinding>(
    Callback(), ItemRecipeReviewBinding::inflate
) {

    class Callback : DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem.commentId == newItem.commentId
        }

        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem == newItem
        }
    }

    override fun bind(item: Review, binding: ItemRecipeReviewBinding, position: Int) = with(binding) {
        super.bind(item, binding, position)
        tvInitials.text = item.username.split(" ").run {
            (first().getOrNull(0)?.toString() ?: "") + (last().getOrNull(0)?.toString() ?: "")
        }
        tvName.text = item.username
        tvText.text = item.commentText
        tvDate.text = item.createdDate.toReadableFullFormat()
        rateView.setRate(item.star)
    }
}