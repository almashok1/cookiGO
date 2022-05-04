package kz.adamant.preferences.data

import kz.adamant.arch.models.Outcome
import kz.adamant.arch.models.ServerSuccessResponse
import kz.adamant.preferences.domain.Preferences
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserPreferencesApi {

    @GET("api/welcomelist")
    suspend fun getPreferences(): Outcome<Preferences>

    @POST("api/submitpreferences")
    suspend fun submitPreferences(@Body request: Preferences): Outcome<ServerSuccessResponse>

}