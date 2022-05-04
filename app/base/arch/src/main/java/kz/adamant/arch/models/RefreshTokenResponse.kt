package kz.adamant.arch.models

import kz.adamant.arch.api.SerializedName

data class RefreshTokenResponse(
    @SerializedName("access_token")
    val accessToken: String?,
    @SerializedName("refresh_token")
    val refreshToken: String?,
    @SerializedName("status")
    val status: String?
)
