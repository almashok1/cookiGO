package kz.adamant.data.api

import kotlinx.coroutines.runBlocking
import kz.adamant.arch.utils.successOrNull
import kz.adamant.data.managers.SessionManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.net.ssl.HttpsURLConnection

class AuthInterceptor(
    private val refreshApi: RefreshApi
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val tokenExpired = SessionManager.accessToken.isEmpty()
        return if (!tokenExpired || SessionManager.refreshToken.isNotEmpty()) {
            // Token is fresh or refresh token exists
            val requestBuilder = chain.request().newBuilder()
            requestBuilder.addHeaders(getToken())
            val originalRequest = chain.request()
            val response = chain.proceed(requestBuilder.build())
            // If token is expired or anyhow received unauthorized then try to refresh
            if (response.code == HttpsURLConnection.HTTP_UNAUTHORIZED || response.code == HttpsURLConnection.HTTP_FORBIDDEN) {
                response.close()
                synchronized(this) {
                    if (updateTokens()) {
                        // Token refreshed, try again
                        val newCall = chain.request().newBuilder().addHeaders(getToken()).build()
                        chain.proceedDeletingTokenOnError(newCall)
                    } else {
                        // Token was expired and can't be refreshed, return
                        chain.proceedDeletingTokenOnError(originalRequest)
                    }
                }
            } else {
                // Response was successful
                response
            }
        } else {
            // Token has expired and there is no refresh token
            chain.proceedDeletingTokenOnError(chain.request())
        }
    }

    private fun getToken(): String {
        return SessionManager.accessToken
    }

    private fun updateTokens(): Boolean = runBlocking {
        return@runBlocking SessionManager.refreshToken.isNotEmpty() && refreshApi.refreshToken(
            RefreshTokenRequest(SessionManager.refreshToken)
        ).successOrNull()?.let(SessionManager::refreshToken) ?: false
    }

    private fun Interceptor.Chain.proceedDeletingTokenOnError(request: Request): Response {
        val response = proceed(request)
        if (
            response.code == HttpsURLConnection.HTTP_UNAUTHORIZED
            || response.code == HttpsURLConnection.HTTP_FORBIDDEN
            || response.code == HttpsURLConnection.HTTP_BAD_REQUEST
        ) {
            SessionManager.clearSession()
        }
        return response
    }

    private fun Request.Builder.addHeaders(token: String) = this.apply { header("accessToken", token) }

}
