package kz.adamant.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kz.adamant.arch.models.Outcome
import kz.adamant.arch.models.OutcomeState
import kz.adamant.arch.utils.onError
import kz.adamant.arch.utils.onResponseError
import kz.adamant.arch.utils.onSuccess

sealed class LoadUntil<out T> {
    object ServerError: LoadUntil<Nothing>()
    data class Condition<T>(val condition: (Outcome<List<T>>) -> Boolean): LoadUntil<T>()
    data class Page(val maxPage: Int): LoadUntil<Nothing>()
}

open class BasicPaging<T> (
    private val request: suspend (page: Int) -> Outcome<List<T>>,
    private val until: LoadUntil<T> = LoadUntil.ServerError
) {

    private val mutex = Mutex()

    private var currentPage = 1 // Page to be loaded
    private var reachedEnd = false // When to stop
    private var loading = false // Loading other data

    private val _state = MutableStateFlow<OutcomeState<Nothing>>(OutcomeState.Idle) // Observable state
    val outcomeState: StateFlow<OutcomeState<Nothing>> = _state

    private val _flow = MutableStateFlow(listOf<T>()) // Observable items
    val flow: StateFlow<List<T>> = _flow

    /**
     * Loads next page
     * @param reset when true loads first page and clears items
     */
    suspend fun loadNext(reset: Boolean = false) {
        mutex.withLock {
            if (loading || (reachedEnd && !reset)) return
            loading = true
            _state.value = OutcomeState.Loading
            val res = request(currentPage)
            when(until) {
                is LoadUntil.Condition -> if (until.condition(res)) reachedEnd = true
                is LoadUntil.Page -> if (currentPage>=until.maxPage) reachedEnd = true
                LoadUntil.ServerError -> res.onResponseError { _, _ -> reachedEnd = true }
            }
            res.onSuccess { data ->
                val items: MutableList<T>
                if (reset) {
                    items = mutableListOf()
                    reachedEnd = false
                } else {
                    items = _flow.value.toMutableList()
                }
                if (data == null || data.isEmpty()) {
                    reachedEnd = true
                    if (reset) _flow.value = items
                } else {
                    items.addAll(data)
                    _flow.value = items
                }
                _state.value = OutcomeState.Idle
                currentPage++
            }.onError {
                _state.value = OutcomeState.Error(it)
            }
            loading = false
        }
    }
}