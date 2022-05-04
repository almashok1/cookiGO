package kz.adamant.checklist.ui

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import androidx.recyclerview.widget.DiffUtil
import kz.adamant.common.R
import kz.adamant.checklist.databinding.ItemChecklistBinding
import kz.adamant.checklist.domain.Checklist
import kz.adamant.common.binding.BindingBasicAsyncRvAdapter
import kz.adamant.common.extensions.getColorCompat
import kz.adamant.common.extensions.getIngredientDrawableFromPosition
import kz.adamant.common.extensions.onSafeClick
import kz.adamant.common.extensions.strikeThrough

class ChecklistAdapter(
    private val listener: ChecklistListener
) : BindingBasicAsyncRvAdapter<Checklist, ItemChecklistBinding>(
    Callback(), ItemChecklistBinding::inflate
) {

    class Callback : DiffUtil.ItemCallback<Checklist>() {
        override fun areItemsTheSame(oldItem: Checklist, newItem: Checklist): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Checklist, newItem: Checklist): Boolean {
            return oldItem == newItem
        }
    }

    @SuppressLint("SetTextI18n")
    override fun bind(item: Checklist, binding: ItemChecklistBinding, position: Int) = with(binding) {
        val color = root.context.getColorCompat(if (!item.isChecked) R.color.text_edit else R.color.text_disabled)
        vCheck.isActivated = item.isChecked
        vCheck.onSafeClick { listener.checkChecklist(item, !vCheck.isActivated) }
        vDelete.onSafeClick { listener.removeChecklist(item) }
        vMinus.onSafeClick {
            if (!item.isChecked)
                listener.updateChecklist(item, (item.quantity - 1).coerceAtLeast(1))
        }
        vPlus.onSafeClick {
            if (!item.isChecked)
                listener.updateChecklist(item, (item.quantity + 1).coerceAtLeast(1))
        }

        tvName.text = item.name
        tvQuantityUnit.text = "${item.quantity} ${item.measureUnit.mini}"
        tvName.strikeThrough(item.isChecked)

        vCheck.imageTintList = ColorStateList.valueOf(color)
        vDelete.imageTintList = ColorStateList.valueOf(color)
        vMinus.imageTintList = ColorStateList.valueOf(color)
        vPlus.imageTintList = ColorStateList.valueOf(color)
        tvName.setTextColor(color)
        tvQuantityUnit.setTextColor(color)

        root.setBackgroundResource(getIngredientDrawableFromPosition(position, itemCount))
    }
}

interface ChecklistListener {
    fun updateChecklist(checklist: Checklist, updatedCount: Int)
    fun removeChecklist(checklist: Checklist)
    fun checkChecklist(checklist: Checklist, isChecked: Boolean)
    fun addChecklist(checklist: Checklist)
}