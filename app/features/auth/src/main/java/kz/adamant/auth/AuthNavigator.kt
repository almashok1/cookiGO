package kz.adamant.auth

import androidx.fragment.app.Fragment

interface AuthNavigator {
    fun openMainScreen(fragment: Fragment, fromSignUp: Boolean)
    fun openAuthScreen(fragment: Fragment)
}