package kz.adamant.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrentUser(
    val email: String,
    val name: String
): Parcelable