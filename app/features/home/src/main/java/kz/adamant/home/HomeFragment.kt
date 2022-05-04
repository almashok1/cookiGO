package kz.adamant.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import kz.adamant.common.BaseViewModel
import kz.adamant.common.binding.BindingFragment
import kz.adamant.common.extensions.getSlideAnimNavBuilder
import kz.adamant.common.extensions.setUpShimmerVisibility
import kz.adamant.data.managers.SessionManager
import kz.adamant.home.databinding.FragmentHomeBinding
import kz.adamant.recipe.ui.adapters.RecipeFullAdapter
import kz.adamant.recipe.ui.adapters.bindRecipeSmall
import kz.adamant.recipe.ui.models.RecipeDvo
import kz.adamant.recipe.ui.models.RecommendationDvo
import kotlin.reflect.KClass

class HomeFragment : BindingFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    override fun provideViewModel(): Map<KClass<*>, () -> BaseViewModel> = mapOf(
        vmCreator(HomeViewModel::class)
    )

    private val vm get() = vm(HomeViewModel::class)

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        RecipeFullAdapter(
            onClick = {
                vm?.openRecipeDetail(it)
            },
            onClickFavourite = {
                vm?.handleFavouriteAction(it)
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vShimmerDailyRec.setUpShimmerVisibility()
        binding.vShimmerRec.setUpShimmerVisibility()
        binding.rvRecommendations.adapter = adapter
        vm?.run {
            binding.tvLetsCook.text = getString(
                kz.adamant.common.R.string.home_intro,
                SessionManager.username
            )
            recommendations.observe(viewLifecycleOwner, ::bindRecommendations)
            loading.observe(viewLifecycleOwner, ::bindLoading)
            getRecommendations()
        }
    }

    private fun bindRecommendations(dvo: RecommendationDvo) = with(binding) {
        dvo.daily?.let(::bindDailyRecommendation)
        adapter.submitList(dvo.recommendations)
    }

    private fun bindDailyRecommendation(dvo: RecipeDvo) = with(binding.vDailyRec) {
        root.isVisible = true
        bindRecipeSmall(
            dvo,
            onClick = {
                vm?.openRecipeDetail(it)
            },
            onFavouriteClick = {
                vm?.handleFavouriteAction(it)
            }
        )
    }

    private fun bindLoading(loading: Boolean) = with(binding) {
        vDailyRec.root.isVisible = !loading
        vShimmerDailyRec.isVisible = loading
        rvRecommendations.isInvisible = loading
        vShimmerRec.isVisible = loading
    }
}
