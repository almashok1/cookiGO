package kz.adamant.checklist.ui

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kz.adamant.checklist.domain.Checklist
import kz.adamant.checklist.domain.ChecklistInteractor
import kz.adamant.checklist.domain.MeasureUnit
import kz.adamant.common.BaseViewModel

class ChecklistViewModel(
    checklistInteractor: ChecklistInteractor
) : ChecklistAbstractViewModel(checklistInteractor) {

    var checklistShowEmpty = true

    val checklists = checklistInteractor.flow.asLiveData(viewModelScope.coroutineContext)

}