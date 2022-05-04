package kz.adamant.recipe

import androidx.fragment.app.Fragment
import kz.adamant.recipe.ui.models.RecipeDvo

interface RecipeNavigator {

    fun openRecipeDetails(fragment: Fragment, recipeDvo: RecipeDvo)

    fun goToHome(fragment: Fragment)

}