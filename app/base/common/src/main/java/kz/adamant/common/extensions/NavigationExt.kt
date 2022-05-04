package kz.adamant.common.extensions

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavGraph
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import kz.adamant.common.R

fun NavController.navigateWithAnim(
    @IdRes resId: Int,
    navOptions: NavOptions.Builder = getSlideAnimNavBuilder()
) = navigate(resId, null, navOptions.build())

fun NavController.navigateWithAnim(
    directions: NavDirections,
    navOptions: NavOptions.Builder = getSlideAnimNavBuilder()
) = navigate(directions, navOptions.build())

fun getSlideAnimNavBuilder() = NavOptions.Builder()
    .setEnterAnim(R.anim.slide_left)
    .setExitAnim(R.anim.slide_left)
    .setPopEnterAnim(R.anim.slide_right)
    .setPopExitAnim(R.anim.slide_right)

fun getSlideAnimUpNavBuilder() = NavOptions.Builder()
    .setEnterAnim(R.anim.slide_up)
    .setExitAnim(R.anim.slide_down)
    .setPopEnterAnim(R.anim.slide_up)
    .setPopExitAnim(R.anim.slide_down)

fun <T> Fragment.observeSavedState(
    key: String,
    navController: NavController = findNavController(),
    handle: (T) -> Unit
) {
    navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<T>(key)
        ?.observe(viewLifecycleOwner) {
            handle(it)
        }
}

fun <T> Fragment.removeSavedStateObserver(
    key: String,
    navController: NavController = findNavController(),
    handle: (T) -> Unit
) {
    navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<T>(key)
        ?.removeObserver(handle)
}

fun <T> Fragment.removeAllSavedStateObservers(
    key: String,
    navController: NavController = findNavController()
) {
    navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<T>(key)
        ?.removeObservers(viewLifecycleOwner)
}

fun <T> Fragment.setToPrevSavedState(
    key: String,
    data: T,
    navController: NavController = findNavController()
) {
    navController.previousBackStackEntry?.savedStateHandle?.set(key, data)
}

fun <T> Fragment.setToCurrentSavedState(
    key: String,
    data: T,
    navController: NavController = findNavController()
) {
    navController.currentBackStackEntry?.savedStateHandle?.set(key, data)
}

fun NavController.navigateSafe(@IdRes resId: Int, args: Bundle? = null) {
    val destinationId = currentDestination?.getAction(resId)?.destinationId
    currentDestination?.let { node ->
        val currentNode = when (node) {
            is NavGraph -> node
            else -> node.parent
        }
        destinationId?.let {
            currentNode?.findNode(destinationId)?.let { navigate(resId, args) }
        }
    }
}

fun NavController.navigateSafe(directions: NavDirections) {
    navigateSafe(directions.actionId, directions.arguments)
}
