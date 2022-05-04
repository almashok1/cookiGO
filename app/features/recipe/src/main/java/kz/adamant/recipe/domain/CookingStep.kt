package kz.adamant.recipe.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kz.adamant.arch.api.SerializedName

@Parcelize
data class CookingStep(
    @SerializedName("step_number") val stepNumber: Int,
    @SerializedName("text") val text: String,
    @SerializedName("duration") val duration: Long?,
    @SerializedName("image") val image: String?
) : Parcelable