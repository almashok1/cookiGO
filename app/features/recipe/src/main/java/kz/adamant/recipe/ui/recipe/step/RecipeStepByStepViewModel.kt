package kz.adamant.recipe.ui.recipe.step

import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kz.adamant.common.BaseViewModel
import kz.adamant.common.extensions.getState
import kz.adamant.common.extensions.liveData
import kz.adamant.recipe.domain.CookingStep
import kz.adamant.recipe.domain.RecipeInteractor
import kz.adamant.recipe.ui.recipe.RecipeDetailFragmentDirections

class RecipeStepByStepViewModel(
    private val recipeInteractor: RecipeInteractor
) : BaseViewModel() {

    private val _steps = getState<List<CookingStep>>(Dispatchers.IO)
    val steps get() = _steps.liveData

    fun getSteps(recipeId: Long) {
        _steps.request {
            recipeInteractor.getRecipeSteps(recipeId).apply {
                handleError()
            }
//                .doOnError {
//                    _steps.setMain(listOf(
//                        CookingStep(1, "Hey Hey Lorem Ipsum dolar sit amet hahah ahsdaj n", 0),
//                        CookingStep(2, "Hey Hey Lorem Ipsum dolar sit amet hahah ahsdaj n Hey Hey Lorem Ipsum dolar sit amet hahah ahsdaj n Hey Hey Lorem Ipsum dolar sit amet hahah ahsdaj n", 100),
//                        CookingStep(3, "Hey Hey Lorem Ipsum dolar sit amet hahah ahsdaj n", 100),
//                        CookingStep(4, "Hey Hey Lorem Ipsum dolar sit amet hahah ahsdaj n", 0),
//                    ))
//                }
        }
    }

    fun showDetail(step: CookingStep) = withFragment {
        it.findNavController().navigate(
            RecipeDetailFragmentDirections.actionRecipeDetailFragmentToRecipeStepDetailFragment(step)
        )
    }

}