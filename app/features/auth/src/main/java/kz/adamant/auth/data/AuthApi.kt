package kz.adamant.auth.data

import kz.adamant.arch.models.Outcome
import kz.adamant.arch.models.ServerSuccessResponse
import kz.adamant.auth.domain.SignIn
import kz.adamant.auth.data.models.SignInResponse
import kz.adamant.auth.domain.SignUp
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("api/signup")
    suspend fun signUp(
        @Body body: SignUp
    ): Outcome<ServerSuccessResponse>

    @POST("api/signin")
    suspend fun signIn(
        @Body body: SignIn
    ): Outcome<SignInResponse>

}