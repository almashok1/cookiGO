package kz.adamant.common

import androidx.fragment.app.Fragment
import androidx.navigation.NavController

interface MainNavControllerFinder {

    operator fun invoke(fragment: Fragment): NavController

}