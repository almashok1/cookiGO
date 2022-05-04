package kz.adamant.checklist.domain

import kotlinx.coroutines.flow.MutableStateFlow

interface ChecklistHandler {

    val flow: MutableStateFlow<List<Checklist>>

    fun addChecklist(checklist: Checklist) {
        var foundOne = false
        val new = flow.value.map {
            if (it.name == checklist.name) {
                foundOne = true
                it.copy(quantity = checklist.quantity + it.quantity)
            }
            else {
                it
            }
        }
        flow.value = if (!foundOne) new + checklist else new
        doAfterAction()
    }

    fun removeChecklist(checklist: Checklist) {
        flow.value = flow.value.filterNot {
            it.name == checklist.name
        }
        doAfterAction()
    }

    fun updateChecklist(checklist: Checklist) {
        flow.value = flow.value.map {
            if (it.name == checklist.name) checklist else it
        }
        doAfterAction()
    }

    fun doAfterAction() { }

    fun clear() {
        flow.value = emptyList()
    }
}