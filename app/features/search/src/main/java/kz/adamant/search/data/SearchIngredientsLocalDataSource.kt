package kz.adamant.search.data

import kotlinx.coroutines.flow.MutableStateFlow
import kz.adamant.checklist.domain.Checklist
import kz.adamant.checklist.domain.ChecklistHandler

class SearchIngredientsLocalDataSource : ChecklistHandler {
    override val flow: MutableStateFlow<List<Checklist>> = MutableStateFlow(listOf())
}