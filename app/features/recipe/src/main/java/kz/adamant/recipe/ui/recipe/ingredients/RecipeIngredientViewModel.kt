package kz.adamant.recipe.ui.recipe.ingredients

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kz.adamant.arch.utils.doOnSuccess
import kz.adamant.checklist.domain.Checklist
import kz.adamant.checklist.domain.ChecklistInteractor
import kz.adamant.checklist.domain.MeasureUnit
import kz.adamant.common.BaseViewModel
import kz.adamant.common.extensions.setMain
import kz.adamant.recipe.domain.IngredientRef
import kz.adamant.recipe.domain.RecipeInteractor

class RecipeIngredientViewModel(
    private val recipeInteractor: RecipeInteractor,
    private val checklistInteractor: ChecklistInteractor
) : BaseViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _addAllEnabled = MutableLiveData(true)
    val addAllEnabled: LiveData<Boolean> = _addAllEnabled

    private val _ingredients = MutableLiveData<List<IngredientRef>>()
    val ingredients: LiveData<List<IngredientRef>> = _ingredients

    fun getIngredients(recipeId: Long) = launchIO {
        _loading.setMain(true)
        recipeInteractor.getIngredients(recipeId)
            .doOnSuccess {
                withContext(Dispatchers.Main) {
                    _ingredients.value = it?.ingredientList ?: listOf()
                    _loading.value = false
                }
            }
            .handleError()
    }.bindSubscribe("getIngredients")

    fun addIngredient(ref: IngredientRef, index: Int) = launchIO {
        addChecklist(ref)
        val new = ingredients.value?.map {
            if (it.id == ref.id) it.copy(isEnabled = false) else it
        } ?: listOf()
        _ingredients.setMain(new)
    }

    private fun addChecklist(ref: IngredientRef) {
        checklistInteractor.addChecklist(
            Checklist(
                ref.ingredient.name ?: "",
                ref.quantity.toInt(),
                MeasureUnit.from(ref.ingredient.unitOfMeasurement?.name)
            )
        )
    }

    fun addAllIngredients() = launchIO {
        val new = ingredients.value?.map {
            addChecklist(it)
            it.copy(isEnabled = false)
        } ?: listOf()
        withContext(Dispatchers.Main) {
            _addAllEnabled.value = false
            _ingredients.value = new
        }
    }

    fun updatePortions(portions: Int) {
        val new = ingredients.value?.map {
            it.copy(quantity = it.initialQuantity * portions, isEnabled = true)
        } ?: listOf()
        _ingredients.value = new
        _addAllEnabled.value = true
    }

}