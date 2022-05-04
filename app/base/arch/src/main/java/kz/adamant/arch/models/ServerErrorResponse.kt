package kz.adamant.arch.models

import kz.adamant.arch.api.SerializedName
import kz.adamant.arch.models.ServerErrorBody

data class ServerErrorResponse(
    @SerializedName( "status")
    val status: Int? = null,
    @SerializedName("error")
    val error: String? = null,
    @SerializedName("path")
    val path: String? = null,
    @SerializedName("timestamp")
    val timestamp: String? = null
)