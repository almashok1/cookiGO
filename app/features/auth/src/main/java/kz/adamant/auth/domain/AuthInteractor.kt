package kz.adamant.auth.domain

import kz.adamant.arch.models.Outcome
import kz.adamant.arch.models.ServerSuccessResponse
import kz.adamant.auth.data.AuthApi
import kz.adamant.auth.data.models.SignInResponse

class AuthInteractor(private val api: AuthApi) {
    suspend fun signUp(body: SignUp): Outcome<ServerSuccessResponse> = api.signUp(body)
    suspend fun signIn(body: SignIn): Outcome<SignInResponse> = api.signIn(body)
}