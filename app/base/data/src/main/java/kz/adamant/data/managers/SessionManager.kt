package kz.adamant.data.managers


import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kz.adamant.data.CacheCleaners
import kz.adamant.data.api.RefreshToken
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

const val ARG_ACCESS_TOKEN = "access_token"
const val ARG_REFRESH_TOKEN = "refresh_token"
const val ARG_LOGIN = "login"
const val ARG_USER_ID = "user_id"
const val ARG_EMAIL = "email"
const val ARG_TOKEN_TYPE = "token_type"

object SessionManager: KoinComponent {

    private val sharedPreferences = get<SharedPreferences>()
    private val cacheCleaner = get<CacheCleaners>()
    private val _loggedOut = MutableLiveData(false)
    val loggedOut: LiveData<Boolean> = _loggedOut

    var accessToken: String = ""
        get() {
            return if (field.isEmpty())
                sharedPreferences.getString(ARG_ACCESS_TOKEN, "") ?: ""
            else
                field
        }
        set(value) {
            field = value
            _loggedOut.postValue(false)
            sharedPreferences.edit().putString(ARG_ACCESS_TOKEN, value).apply()
        }

    var refreshToken: String
        get() = sharedPreferences.getString(ARG_REFRESH_TOKEN, "") ?: ""
        set(value) = sharedPreferences.edit().putString(ARG_REFRESH_TOKEN, value).apply()

    var email: String
        get() = sharedPreferences.getString(ARG_EMAIL, "") ?: ""
        set(value) = sharedPreferences.edit().putString(ARG_EMAIL, value).apply()

    var tokenType: String
        get() = sharedPreferences.getString(ARG_TOKEN_TYPE, "") ?: ""
        set(value) = sharedPreferences.edit().putString(ARG_TOKEN_TYPE, value).apply()

    var username: String = ""
        get() = field.ifEmpty { sharedPreferences.getString(ARG_LOGIN, "") ?: "" }
        set(value) {
            field = value
            sharedPreferences.edit().putString(ARG_LOGIN, value).apply()
        }

    var userId: Long = -1
        get() = if (field != -1L) field else sharedPreferences.getLong(ARG_USER_ID, -1)
        set(value) {
            field = value
            sharedPreferences.edit().putLong(ARG_USER_ID, value).apply()
        }

    fun refreshToken(refreshTokenData: RefreshToken): Boolean {
        updateSession(SessionData(
            id = userId,
            email = email,
            tokenType = tokenType,
            username = username,
            accessToken = refreshTokenData.accessToken,
            refreshToken = refreshTokenData.refreshToken
        ))
        return refreshTokenData.refreshToken.isNotEmpty()
    }

    fun updateSession(data: SessionData) {
        userId = data.id
        refreshToken = data.refreshToken
        accessToken = data.accessToken
        tokenType = data.tokenType
        email = data.email
        username = data.username
    }

    fun clearSession(){
        accessToken = ""
        email = ""
        username = ""
        refreshToken = ""
        cacheCleaner.clearAll()
        _loggedOut.postValue(true)
    }

}

class SessionData(
    val id: Long,
    val refreshToken: String,
    val accessToken: String,
    val tokenType: String,
    val username: String,
    val email: String
)