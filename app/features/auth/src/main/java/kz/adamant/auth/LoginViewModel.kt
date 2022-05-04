package kz.adamant.auth

import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kz.adamant.common.BaseViewModel
import kz.adamant.common.extensions.navigateWithAnim
import kz.adamant.data.managers.SessionManager

class LoginViewModel(
    private val authNavigator: AuthNavigator
) : BaseViewModel() {

    init {
        launchIO { checkToken() }
    }

    private suspend fun checkToken() {
        delay(500L)
        withContext(Dispatchers.Main) {
            withFragment {
                if (SessionManager.accessToken.isEmpty()) {
                    it.findNavController().navigateWithAnim(R.id.authOnboardingFragment)
                } else {
                    authNavigator.openMainScreen(it, false)
                }
            }
        }
    }

}