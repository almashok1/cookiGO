package kz.adamant.checklist.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import kz.adamant.checklist.databinding.FragmentChecklistBinding
import kz.adamant.common.BaseViewModel
import kz.adamant.common.binding.BindingFragment
import kz.adamant.common.extensions.beginChangeBoundsTransition
import kotlin.reflect.KClass

class ChecklistFragment : BindingFragment<FragmentChecklistBinding>(
    FragmentChecklistBinding::inflate
) {
    override fun provideViewModel(): Map<KClass<*>, () -> BaseViewModel> = mapOf(
        vmCreator(ChecklistViewModel::class)
    )

    private val vm get() = vm(ChecklistViewModel::class)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setChecklistView()
        binding.vEmpty.setOnActionBtnClick {
            binding.vEmpty.isVisible = false
            binding.vChecklist.isVisible = true
            vm.checklistShowEmpty = false
            binding.vChecklist.beginChangeBoundsTransition()
        }
        vm.checklists.observe(viewLifecycleOwner) {
            binding.vChecklist.submitChecklists(it)
            val isEmpty = vm.checklistShowEmpty && it.isEmpty()
            binding.vEmpty.isInvisible = !isEmpty
            binding.vChecklist.isInvisible = isEmpty
            binding.vChecklist.beginChangeBoundsTransition()
        }
    }

    private fun setChecklistView() = with(binding.vChecklist) {
        this.vm = { this@ChecklistFragment.vm }
    }
}
