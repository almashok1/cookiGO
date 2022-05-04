package kz.adamant.recipe.ui.models

import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import kz.adamant.common.R

@Parcelize
data class RecipeDvo(
    val id: Long,
    val title: String,
    val imageUrl: String,
    val description: String,
    val rate: Int,
    val category: Category,
    val complexity: Complexity,
    val complexityId: Long,
    val timeToCook: Int,
    val calorie: Int,
    val avgComments: Long,
    val isFavourite: Boolean = false
) : Parcelable {
    @Parcelize
    enum class Complexity(
        @ColorRes val background: Int,
        @StringRes val title: Int
    ) : Parcelable {
        EASY(R.color.complexity_easy, R.string.common_easy),
        MEDIUM(R.color.complexity_medium, R.string.common_medium),
        HARD(R.color.complexity_hard, R.string.common_hard),
    }
    @Parcelize
    class Category(
        val id: Long,
        val name: String,
        val image: String
    ) : Parcelable
}