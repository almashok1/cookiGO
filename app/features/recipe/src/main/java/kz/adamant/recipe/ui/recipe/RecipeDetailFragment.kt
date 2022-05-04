package kz.adamant.recipe.ui.recipe

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kz.adamant.common.BaseViewModel
import kz.adamant.common.REQUEST_UPDATE
import kz.adamant.common.binding.BindingFragment
import kz.adamant.common.extensions.loadWithDriveUrl
import kz.adamant.common.extensions.onSafeClick
import kz.adamant.common.extensions.setToPrevSavedState
import kz.adamant.recipe.ui.models.RecipeDvo
import kz.adamant.recipe.ui.recipe.ingredients.RecipeIngredientsFragment
import kz.adamant.recipe.ui.recipe.review.RecipeReviewFragment
import kz.adamant.recipe.ui.recipe.step.RecipeStepByStepFragment
import kz.adamant.recipe.databinding.FragmentRecipeDetailBinding
import kotlin.reflect.KClass

class RecipeDetailFragment : BindingFragment<FragmentRecipeDetailBinding>(
    FragmentRecipeDetailBinding::inflate
) {

    companion object {
        private const val RECIPE_ARGS = "recipe"
        fun getBundle(recipeDvo: RecipeDvo) = bundleOf(RECIPE_ARGS to recipeDvo)
    }

    private val navArgs by navArgs<RecipeDetailFragmentArgs>()

    private val tabs = RecipeTabItem.values()

    override fun provideViewModel(): Map<KClass<*>, () -> BaseViewModel> = mapOf(
        vmCreator(RecipeDetailViewModel::class)
    )

    private val vm get() = vm(RecipeDetailViewModel::class)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.setRecipe(navArgs.recipe)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.tvPortionPlus.onSafeClick {
            vm.updatePortions(vm.portions.value!! + 1)
        }
        binding.tvPortionMinus.onSafeClick {
            vm.updatePortions(vm.portions.value!! - 1)
        }
        binding.vp2.adapter = Vp2Adapter(this, navArgs.recipe, tabs, vm.portions) {
            binding.tvPortionsSize.text = it.toString()
        }
        vm.recipe.observe(viewLifecycleOwner) {
            bindRecipeMainDetails(it)
        }
        TabLayoutMediator(binding.vTabs, binding.vp2) { tab, position ->
            tab.text = tabs[position].getTitle()
        }.attach()
        binding.vp2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.btnLeaveFeedback.isVisible = position == 2
            }
        })
        binding.btnLeaveFeedback.onSafeClick {
            findNavController().navigate(
                RecipeDetailFragmentDirections.actionRecipeDetailFragmentToRecipeLeaveFeedbackFragment(navArgs.recipe.id)
            )
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindRecipeMainDetails(recipe: RecipeDvo) = with(binding) {
        tvRecipeName.text = recipe.title
        ivFavourite.isActivated = recipe.isFavourite
        ivFavourite.onSafeClick { vm.toggleFavourite() }
        tvRate.text = recipe.rate.toString()
        ivPhoto.loadWithDriveUrl(recipe.imageUrl)
        tvCommentsCount.text = "(${recipe.avgComments})"
        tvComplexity.text = getString(recipe.complexity.title).lowercase()
        tvCalorie.text =
            getString(kz.adamant.common.R.string.common_calorie, recipe.calorie.toString())
        tvCookTime.text =
            getString(kz.adamant.common.R.string.common_cookingTime, recipe.timeToCook.toString())
        tvDescription.text = recipe.description
    }

    private class Vp2Adapter(
        private val fragment: Fragment,
        private val recipe: RecipeDvo,
        private val items: Array<RecipeTabItem>,
        private val portions: LiveData<Int>,
        private val onPortionUpdated: (Int) -> Unit
    ) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = items.size

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> RecipeIngredientsFragment.newInstance(recipe.id).apply {
                    portions.observe(fragment.viewLifecycleOwner) {
                        onPortionUpdated(it)
                        this.updatePortions(it)
                    }
                }
                1 -> RecipeStepByStepFragment.newInstance(recipe.id)
                else -> RecipeReviewFragment.newInstance(recipe.id)
            }
        }
    }

    enum class RecipeTabItem {
        INGREDIENTS, STEP_BY_STEP, REVIEW
    }

    private fun RecipeTabItem.getTitle() = getString(
        when (this) {
            RecipeTabItem.INGREDIENTS -> kz.adamant.common.R.string.recipe_detail_ingredients
            RecipeTabItem.STEP_BY_STEP -> kz.adamant.common.R.string.recipe_detail_stepByStep
            RecipeTabItem.REVIEW -> kz.adamant.common.R.string.recipe_detail_review
        }
    )
}
