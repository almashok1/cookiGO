package kz.adamant.auth.domain

data class SignUp (
    val username: String,
    val email: String,
    val password: String
)

data class SignIn (
    val username: String,
    val password: String
)