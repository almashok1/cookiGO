package kz.adamant.account

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kz.adamant.auth.AuthNavigator
import kz.adamant.common.BaseViewModel
import kz.adamant.data.managers.SessionManager

class AccountViewModel(
    private val authNavigator: AuthNavigator
) : BaseViewModel() {

    fun logout() = launchIO {
        SessionManager.clearSession()
    }

}