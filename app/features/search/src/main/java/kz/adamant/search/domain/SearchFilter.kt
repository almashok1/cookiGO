package kz.adamant.search.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kz.adamant.data.managers.SessionManager

@Parcelize
data class SearchFilter(
    val userId: Long = SessionManager.userId,
    val word: String? = null,
    val calories: Int? = null,
    val complexityId: Int? = null,
    val minCookingTime: Int? = null,
    val maxCookingTime: Int? = null,
    val categoryId: Long? = null,
    val cuisineId: Long? = null,
    val ingredientList: List<String>? = null
) : Parcelable {
    fun mergeWith(filter: SearchFilter, priorityThis: Boolean = false) : SearchFilter {
        val first = if (priorityThis) this else filter
        val second = if (priorityThis) filter else this
        return SearchFilter(
            userId = filter.userId,
            word = first.word ?: second.word,
            calories = first.calories ?: first.calories,
            complexityId = first.complexityId ?: second.complexityId,
            minCookingTime = first.minCookingTime ?: second.minCookingTime,
            maxCookingTime = first.maxCookingTime ?: second.maxCookingTime,
            categoryId = first.categoryId ?: second.categoryId,
            cuisineId = first.cuisineId ?: second.cuisineId
        )
    }
}