package kz.adamant.common

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import kz.adamant.common.extensions.observeSavedState
import kz.adamant.common.extensions.setToCurrentSavedState

const val REQUEST_UPDATE = "req_update"

fun Fragment.observeUpdate(
    navController: NavController = findNavController(),
    update: () -> Unit
) {
    setToCurrentSavedState(REQUEST_UPDATE, false, navController)
    observeSavedState<Boolean>(REQUEST_UPDATE, navController) {
        if (it) update()
    }
}