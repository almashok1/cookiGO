package kz.adamant.recipe.ui.recipe.review

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import kz.adamant.arch.models.OutcomeState
import kz.adamant.common.BaseViewModel
import kz.adamant.common.binding.BindingFragment
import kz.adamant.common.extensions.observeSavedState
import kz.adamant.common.extensions.setToCurrentSavedState
import kz.adamant.recipe.databinding.FragmentReviewBinding
import kz.adamant.recipe.domain.Review
import kotlin.reflect.KClass

class RecipeReviewFragment : BindingFragment<FragmentReviewBinding>(
    FragmentReviewBinding::inflate
) {
    override fun provideViewModel(): Map<KClass<*>, () -> BaseViewModel> = mapOf(
        vmCreator(RecipeReviewViewModel::class)
    )

    private val vm get() = vm(RecipeReviewViewModel::class)!!

    private val adapter = RecipeReviewAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rv.adapter = adapter
        val recipeId = requireArguments().getLong(ARG_RECIPE_ID, -1)
        vm.getReviews(recipeId)
        setToCurrentSavedState(KEY_OBSERVE_ADDED, false)
        observeSavedState<Boolean>(KEY_OBSERVE_ADDED) {
            if (it) vm.getReviews(recipeId)
        }
        vm.reviews.observe(viewLifecycleOwner, ::bindState)
    }

    private fun bindState(state: OutcomeState<List<Review>>) = with(binding) {
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
        const val KEY_OBSERVE_ADDED = "key_add"

        fun newInstance(recipeId: Long) = RecipeReviewFragment().apply {
            arguments = bundleOf(ARG_RECIPE_ID to recipeId)
        }
    }
}