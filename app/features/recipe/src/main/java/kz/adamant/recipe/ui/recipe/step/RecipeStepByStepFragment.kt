package kz.adamant.recipe.ui.recipe.step

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import kz.adamant.arch.models.OutcomeState
import kz.adamant.common.BaseViewModel
import kz.adamant.common.binding.BindingFragment
import kz.adamant.recipe.databinding.FragmentStepByStepBinding
import kz.adamant.recipe.domain.CookingStep
import kz.adamant.recipe.domain.Review
import kotlin.reflect.KClass

class RecipeStepByStepFragment : BindingFragment<FragmentStepByStepBinding>(
    FragmentStepByStepBinding::inflate
) {
    override fun provideViewModel(): Map<KClass<*>, () -> BaseViewModel> = mapOf(
        vmCreator(RecipeStepByStepViewModel::class)
    )

    private val vm get() = vm(RecipeStepByStepViewModel::class)!!

    private val adapter = RecipeStepsAdapter {
        vm.showDetail(it)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.getSteps(requireArguments().getLong(ARG_RECIPE_ID, -1))

        binding.rv.adapter = adapter
        vm.steps.observe(viewLifecycleOwner, ::bindState)
    }

    private fun bindState(state: OutcomeState<List<CookingStep>>) = with(binding) {
        when (state) {
            OutcomeState.Idle, OutcomeState.Loading -> {
                progress.isVisible = true
            }
            is OutcomeState.Success -> {
                progress.isVisible = false
                adapter.submitList(state.data)
            }
            else -> {}
        }
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    companion object {
        private const val ARG_RECIPE_ID = "recipe_id"

        fun newInstance(recipeId: Long) = RecipeStepByStepFragment().apply {
            arguments = bundleOf(ARG_RECIPE_ID to recipeId)
        }
    }

}