package kz.adamant.checklist.ui

import kz.adamant.checklist.domain.Checklist
import kz.adamant.checklist.domain.ChecklistInteractor
import kz.adamant.checklist.domain.MeasureUnit
import kz.adamant.common.BaseViewModel

abstract class ChecklistAbstractViewModel(
    private val checklistInteractor: ChecklistInteractor
) : BaseViewModel() {

    var selectedUnit: MeasureUnit? = null

    fun updateChecklist(checklist: Checklist, updatedCount: Int) = launchIO {
        checklistInteractor.updateChecklist(checklist, updatedCount)
    }

    fun removeChecklist(checklist: Checklist) = launchIO {
        checklistInteractor.removeChecklist(checklist)
    }

    fun addChecklist(checklist: Checklist) = launchIO {
        checklistInteractor.addChecklist(checklist)
    }

    fun checkChecklist(checklist: Checklist, isChecked: Boolean) = launchIO {
        checklistInteractor.checkChecklist(checklist, isChecked)
    }


}