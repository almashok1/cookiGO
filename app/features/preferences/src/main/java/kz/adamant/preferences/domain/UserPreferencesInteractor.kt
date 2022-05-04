package kz.adamant.preferences.domain

import kz.adamant.arch.models.Outcome
import kz.adamant.arch.models.ServerSuccessResponse
import kz.adamant.preferences.data.UserPreferencesApi

class UserPreferencesInteractor(
    private val api: UserPreferencesApi
) {
    suspend fun getPreferences(): Outcome<Preferences> = api.getPreferences()
    suspend fun submitPreferences(request: Preferences): Outcome<ServerSuccessResponse> = api.submitPreferences(request)
}