package kz.adamant.checklist.data

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kz.adamant.arch.api.JsonSerializer
import kz.adamant.checklist.domain.Checklist
import kz.adamant.checklist.domain.ChecklistHandler
import kz.adamant.data.CacheCleaner

class ChecklistLocalDataSource(
    private val jsonSerializer: JsonSerializer,
    private val sharedPreferences: SharedPreferences
): ChecklistHandler, CacheCleaner {
    companion object {
        private const val PREF_CHECKLISTS = "pref_checklists"
    }

    override val flow = MutableStateFlow(
        jsonSerializer.jsonToObject(
            ChecklistWrapper::class.java,
            sharedPreferences.getString(PREF_CHECKLISTS, null)
        )?.checklists ?: listOf()
    )

    val checklistFlow: Flow<List<Checklist>> get() = flow

    override fun doAfterAction() {
        updateDb()
    }

    private fun updateDb() {
        sharedPreferences.edit().putString(PREF_CHECKLISTS, jsonSerializer.objectToJson(
            ChecklistWrapper::class.java,
            ChecklistWrapper(flow.value)
        )).apply()
    }

    override fun clear() {
        super.clear()
        sharedPreferences.edit().putString(PREF_CHECKLISTS, null).apply()
    }

    private class ChecklistWrapper(
        val checklists: List<Checklist>
    )

    override fun clearAll() {
        clear()
    }
}