package kz.adamant.checklist.domain

import kotlinx.coroutines.flow.Flow


interface ChecklistInteractor {
    val flow: Flow<List<Checklist>>

    fun updateChecklist(checklist: Checklist, updatedCount: Int)
    fun removeChecklist(checklist: Checklist)
    fun addChecklist(checklist: Checklist)
    fun checkChecklist(checklist: Checklist, isChecked: Boolean)

    fun clear()

    class Base(
        private val checklistHandler: ChecklistHandler
    ) : ChecklistInteractor {
        override val flow = checklistHandler.flow

        override fun updateChecklist(checklist: Checklist, updatedCount: Int) {
            checklistHandler.updateChecklist(checklist.copy(quantity = updatedCount.coerceAtLeast(0)))
        }
        override fun removeChecklist(checklist: Checklist) {
            checklistHandler.removeChecklist(checklist)
        }
        override fun addChecklist(checklist: Checklist) {
            checklistHandler.addChecklist(checklist)
        }
        override fun checkChecklist(checklist: Checklist, isChecked: Boolean) {
            checklistHandler.updateChecklist(checklist.copy(isChecked = isChecked))
        }

        override fun clear() = checklistHandler.clear()
    }
}
