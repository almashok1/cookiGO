package kz.adamant.auth.data.models

data class SignInResponse (
    val id: Long,
    val username: String,
    val email: String,
    val tokenType: String,
    val accessToken: String,
    val refreshToken: String
)