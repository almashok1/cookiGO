package kz.adamant.recipe.domain

import kz.adamant.arch.api.SerializedName
import java.time.OffsetDateTime
import java.util.Date

data class Review(
    @SerializedName("comment_id") val commentId: Long,
    @SerializedName("username") val username: String,
    @SerializedName("star") val star: Int,
    @SerializedName("comment_text") val commentText: String,
    @SerializedName("created_date") val createdDate: Date
)

class LeaveReviewRequest(
    @SerializedName("user_id") val userId: Long,
    @SerializedName("recipe_id") val recipeId: Long,
    @SerializedName("comment_text") val commentText: String?,
    @SerializedName("star") val star: Int,
)