package kz.adamant.data.api

import kz.adamant.arch.models.Outcome
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RefreshApi {

    @POST("api/refreshtoken")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Outcome<RefreshToken>

}

data class RefreshToken(
    val refreshToken: String,
    val accessToken: String,
    val tokenType: String
)

data class RefreshTokenRequest(
    val refreshToken: String
)