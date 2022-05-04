package kz.adamant.checklist.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Checklist(
    val name: String,
    val quantity: Int,
    val measureUnit: MeasureUnit,
    val isChecked: Boolean = false
) : Parcelable