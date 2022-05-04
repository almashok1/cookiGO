package kz.adamant.preferences

import androidx.recyclerview.widget.DiffUtil
import kz.adamant.common.binding.BindingBasicAsyncRvAdapter
import kz.adamant.common.extensions.dpToPx
import kz.adamant.common.extensions.loadWithDriveUrl
import kz.adamant.preferences.databinding.ItemPreferenceBinding
import kz.adamant.preferences.domain.Preferences

class UserPreferencesAdapter(
    private val onClick: (item: Preferences.Item) -> Unit
) : BindingBasicAsyncRvAdapter<Preferences.Item, ItemPreferenceBinding>(
    Callback(), ItemPreferenceBinding::inflate
) {
    private val selectedItemsIndices = mutableSetOf<Int>()

    fun getSelectedItems() = items.filterIndexed { index, _ -> index in selectedItemsIndices }

    override fun bind(item: Preferences.Item, binding: ItemPreferenceBinding, pos: Int) = with(binding) {
        tvText.text = item.name
        ivImage.loadWithDriveUrl(item.image)
        root.strokeWidth = if (selectedItemsIndices.contains(pos)) 2.dpToPx.toInt() else 0
        root.setOnClickListener {
            selectedItemsIndices.run {
                if (selectedItemsIndices.contains(pos)) remove(pos) else add(pos)
            }
            onClick(item)
            notifyItemChanged(pos)
        }
    }

    class Callback : DiffUtil.ItemCallback<Preferences.Item>() {
        override fun areItemsTheSame(
            oldItem: Preferences.Item,
            newItem: Preferences.Item
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Preferences.Item,
            newItem: Preferences.Item
        ) = oldItem == newItem
    }

}