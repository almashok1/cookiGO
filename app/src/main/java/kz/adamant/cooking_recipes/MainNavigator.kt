package kz.adamant.cooking_recipes

import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import kz.adamant.auth.AuthNavigator
import kz.adamant.common.MainNavControllerFinder
import kz.adamant.common.extensions.getSlideAnimNavBuilder
import kz.adamant.common.extensions.getSlideAnimUpNavBuilder
import kz.adamant.common.extensions.navigateWithAnim
import kz.adamant.preferences.UserPreferencesNavigator
import kz.adamant.recipe.RecipeNavigator
import kz.adamant.recipe.ui.models.RecipeDvo
import kz.adamant.recipe.ui.recipe.RecipeDetailFragment
import kz.adamant.search.SearchNavigator

class AuthNavigatorImpl : AuthNavigator {

    override fun openMainScreen(fragment: Fragment, fromSignUp: Boolean) {
        val navController = fragment.findMainNavController()
        val navOptions = getSlideAnimNavBuilder()
            .setPopUpTo(kz.adamant.auth.R.id.auth_navigation, true)
        if (fromSignUp) navController.navigateWithAnim(
            R.id.action_global_user_preferences_screen,
            navOptions
        )
        else navController.navigateWithAnim(R.id.action_global_main_feature_screen, navOptions)
    }

    override fun openAuthScreen(fragment: Fragment) {
        val navController = fragment.findMainNavController()
        val navOptions = getSlideAnimNavBuilder()
            .setPopUpTo(kz.adamant.auth.R.id.auth_navigation, true)
        navController.navigateWithAnim(R.id.action_global_auth_screen, navOptions)
    }

}

class UserPreferencesNavigatorImpl : UserPreferencesNavigator {

    override fun openMain(fragment: Fragment) {
        fragment.findMainNavController().navigateWithAnim(
            R.id.action_global_main_feature_screen,
            navOptions = getSlideAnimNavBuilder()
                .setPopUpTo(kz.adamant.preferences.R.id.user_preferences_navigation, true)
        )
    }

}

class RecipeNavigatorImpl : RecipeNavigator {
    override fun openRecipeDetails(fragment: Fragment, recipeDvo: RecipeDvo) {
        fragment.findMainNavController().navigate(
            kz.adamant.recipe.R.id.recipe_navigation,
            RecipeDetailFragment.getBundle(recipeDvo),
            getSlideAnimNavBuilder().build(),
        )
    }

    override fun goToHome(fragment: Fragment) {
        fragment.findNavController().navigate(
            kz.adamant.home.R.id.home_navigation
        )
    }

}

class SearchNavigatorImpl : SearchNavigator {
    override fun openSearch(fragment: Fragment) {
        fragment.findMainNavController().navigate(
            kz.adamant.search.R.id.search_navigation,
            null,
            getSlideAnimUpNavBuilder().build(),
        )
    }
}

fun Fragment.findMainNavController() =
    requireActivity().findNavController(R.id.main_nav_host_fragment)

class MainNavControllerFinderImpl : MainNavControllerFinder {
    override fun invoke(fragment: Fragment) = fragment.findMainNavController()

}