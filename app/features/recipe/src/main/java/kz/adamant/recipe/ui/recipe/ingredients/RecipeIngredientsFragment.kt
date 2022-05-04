package kz.adamant.recipe.ui.recipe.ingredients

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import kz.adamant.common.BaseViewModel
import kz.adamant.common.binding.BindingFragment
import kz.adamant.common.extensions.onSafeClick
import kz.adamant.recipe.databinding.FragmentRecipeIngredientsBinding
import kotlin.reflect.KClass

class RecipeIngredientsFragment : BindingFragment<FragmentRecipeIngredientsBinding>(
    FragmentRecipeIngredientsBinding::inflate
) {
    override fun provideViewModel(): Map<KClass<*>, () -> BaseViewModel> = mapOf(
        vmCreator(RecipeIngredientViewModel::class)
    )

    private val vm get() = vm(RecipeIngredientViewModel::class)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm?.getIngredients(requireArguments().getLong(ARG_RECIPE_ID, -1))
        val adapter = RecipeIngredientAdapter { ref, pos ->
            vm?.addIngredient(ref, pos)
        }
        binding.rv.adapter = adapter
        vm?.loading?.observe(viewLifecycleOwner) {
            binding.progress.isVisible = it
        }
        vm?.addAllEnabled?.observe(viewLifecycleOwner) {
            binding.tvAddAll.isVisible = it
        }
        vm?.ingredients?.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.tvAddAll.isVisible = it.isNotEmpty()
        }
        updatePortions(1)
        binding.tvAddAll.onSafeClick {
            vm?.addAllIngredients()
        }
    }

    fun updatePortions(portionSize: Int) {
        vm?.updatePortions(portionSize)
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    companion object {
        private const val ARG_RECIPE_ID = "recipe_id"

        fun newInstance(recipeId: Long) = RecipeIngredientsFragment().apply {
            arguments = bundleOf(ARG_RECIPE_ID to recipeId)
        }
    }
}