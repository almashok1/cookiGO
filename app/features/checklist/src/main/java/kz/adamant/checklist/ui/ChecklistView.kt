package kz.adamant.checklist.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.widget.doAfterTextChanged
import kz.adamant.checklist.databinding.ItemQuickAddBinding
import kz.adamant.checklist.databinding.ItemUnitMenuBinding
import kz.adamant.checklist.databinding.ViewChecklistBinding
import kz.adamant.checklist.domain.Checklist
import kz.adamant.checklist.domain.MeasureUnit
import kz.adamant.common.R
import kz.adamant.common.extensions.onSafeClick

class ChecklistView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayoutCompat(context, attrs), ChecklistListener {

    private val binding = ViewChecklistBinding.inflate(LayoutInflater.from(context), this, true)

    private val adapter = ChecklistAdapter(this)

    var vm: () -> ChecklistAbstractViewModel? = { null }

    private val quickAdds = listOf(
        Triple(R.drawable.image_chicken, R.string.common_chicken, MeasureUnit.KILOGRAM),
        Triple(R.drawable.image_potato, R.string.common_potato, MeasureUnit.KILOGRAM),
        Triple(R.drawable.image_milk, R.string.common_milk, MeasureUnit.LITER),
        Triple(R.drawable.image_carrot, R.string.common_carrot, MeasureUnit.KILOGRAM)
    )

    init {
        val menuAdapter = MenuAdapter(context, MeasureUnit.values())
        binding.btnAdd.isEnabled = false
        binding.tvUnits.setAdapter(menuAdapter)
        binding.tvUnits.setOnItemClickListener { _, _, pos, _ ->
            val unit = menuAdapter.getItem(pos)
            vm()?.selectedUnit = unit
            binding.tvUnits.setText(unit.full)
            checkAddForEnabled()
        }
        binding.rv.adapter = adapter

        binding.vgQuickAdd.weightSum = quickAdds.size.toFloat()
        quickAdds.forEach(::inflateQuickAdd)
        setupAdd()
    }

    private fun inflateQuickAdd(item: Triple<Int, Int, MeasureUnit>) {
        val vBinding = ItemQuickAddBinding.inflate(LayoutInflater.from(context), binding.vgQuickAdd, false)
        vBinding.ivIcon.setImageResource(item.first)
        vBinding.tvText.text = context.getString(item.second)
        vBinding.root.onSafeClick {
            val checklist = Checklist(context.getString(item.second), 1, item.third)
            addChecklist(checklist)
        }
        binding.vgQuickAdd.addView(vBinding.root)
    }

    private fun setupAdd() = with(binding) {
        etName.doAfterTextChanged { checkAddForEnabled() }
        etQuantity.doAfterTextChanged { checkAddForEnabled() }
        btnAdd.onSafeClick {
            val (name, quantity, unit) = collectData()
            reset()
            addChecklist(Checklist(name!!, quantity!!, unit!!))
        }
    }

    private fun reset() = with(binding) {
        etName.text = null
        etQuantity.text = null
        vm()?.selectedUnit = null
        tvUnits.text = null
        tvUnits.clearFocus()
        tilUnits.clearFocus()
    }

    private fun checkAddForEnabled() = with(binding) {
        val (name, quantity, unit) = collectData()
        val isEnabled = name != null && quantity != null && unit != null
        btnAdd.isEnabled = isEnabled
    }

    private fun collectData() : Triple<String?, Int?, MeasureUnit?> = with(binding) {
        val name = etName.text?.toString()
        val quantity = etQuantity.text?.toString()
        val unit = vm()?.selectedUnit
        return Triple(name, quantity?.toIntOrNull(), unit)
    }

    fun submitChecklists(list: List<Checklist>) {
        adapter.submitList(list)
    }

    override fun updateChecklist(checklist: Checklist, updatedCount: Int) {
        vm()?.updateChecklist(checklist, updatedCount)
    }

    override fun removeChecklist(checklist: Checklist) {
        vm()?.removeChecklist(checklist)
    }

    override fun checkChecklist(checklist: Checklist, isChecked: Boolean) {
        vm()?.checkChecklist(checklist, isChecked)
    }

    override fun addChecklist(checklist: Checklist) {
        vm()?.addChecklist(checklist)
    }

}

class MenuAdapter(
    context: Context,
    private val items: Array<MeasureUnit>
) : ArrayAdapter<MeasureUnit>(context, kz.adamant.checklist.R.layout.item_unit_menu, items) {

    override fun getCount(): Int = items.size

    override fun areAllItemsEnabled() = true

    override fun hasStableIds() = true

    override fun getItem(position: Int): MeasureUnit = items[position]

    override fun getItemId(position: Int): Long = items[position].ordinal.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = convertView?.let(ItemUnitMenuBinding::bind) ?:
        ItemUnitMenuBinding.inflate(LayoutInflater.from(context), parent, false)
        binding.root.text = items[position].full
        return binding.root
    }

}