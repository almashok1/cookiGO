package kz.adamant.auth.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kz.adamant.arch.utils.doOnComplete
import kz.adamant.arch.utils.doOnSuccess
import kz.adamant.arch.utils.mapCheckNull
import kz.adamant.arch.utils.onSuccess
import kz.adamant.auth.AuthNavigator
import kz.adamant.auth.data.models.SignInResponse
import kz.adamant.auth.domain.AuthInteractor
import kz.adamant.auth.domain.SignIn
import kz.adamant.auth.domain.SignUp
import kz.adamant.common.BaseViewModel
import kz.adamant.common.extensions.setMain
import kz.adamant.data.managers.SessionData
import kz.adamant.data.managers.SessionManager

class AuthViewModel(
    private val authInteractor: AuthInteractor,
    private val authNavigator: AuthNavigator
) : BaseViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun signUp(
        email: String,
        username: String,
        password: String,
    ) {
        _loading.value = true
        launchIO {
            authInteractor.signUp(SignUp(username, email, password))
                .doOnSuccess {
                    signInSuspended(username, password, true)
                }
                .doOnComplete {
                    _loading.setMain(false)
                }
                .handleError()
        }
    }

    private suspend fun signInSuspended(username: String, password: String, fromSignUp: Boolean = false) {
        authInteractor.signIn(SignIn(username, password))
            .mapCheckNull()
            .onSuccess {
                SessionManager.updateSession(it!!.toSessionData())
                withFragment { fragment ->
                    authNavigator.openMainScreen(fragment, fromSignUp)
                }
            }
            .handleError()
    }

    fun signIn(username: String, password: String) {
        _loading.value = true
        launchIO {
            signInSuspended(username, password)
            _loading.setMain(false)
        }
    }

}

fun SignInResponse.toSessionData() = SessionData(
    refreshToken = refreshToken,
    accessToken = accessToken,
    tokenType = tokenType,
    username = username,
    email = email,
    id = id
)