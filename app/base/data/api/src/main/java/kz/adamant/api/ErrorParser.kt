package kz.adamant.api

import kz.adamant.arch.api.JsonSerializer
import kz.adamant.arch.models.ServerErrorResponse
import kz.adamant.arch.utils.fromJson

fun String?.parseResponseBodyError(serializer: JsonSerializer): String? {
    return if (this.isNullOrBlank()) null
    else {
        val errorBody = serializer.fromJson<ServerErrorResponse>(this)
        errorBody?.error
    }
}