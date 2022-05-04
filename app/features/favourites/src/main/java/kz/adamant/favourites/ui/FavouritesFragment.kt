package kz.adamant.favourites.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import kz.adamant.common.BaseViewModel
import kz.adamant.common.binding.BindingFragment
import kz.adamant.common.extensions.setUpShimmerVisibility
import kz.adamant.common.R
import kz.adamant.favourites.databinding.FragmentFavouritesBinding
import kz.adamant.recipe.ui.adapters.RecipeSmallAdapter
import kz.adamant.recipe.ui.models.RecipeDvo
import kotlin.reflect.KClass

class FavouritesFragment : BindingFragment<FragmentFavouritesBinding>(
    FragmentFavouritesBinding::inflate
){
    override fun provideViewModel(): Map<KClass<*>, () -> BaseViewModel> = mapOf(
        vmCreator(FavouritesViewModel::class)
    )

    private val vm get() = vm(FavouritesViewModel::class)!!

    private val adapter = RecipeSmallAdapter(
        onClick = {
            vm.openRecipeDetail(it)
        },
        onFavouriteClick = {
            vm.removeFavourite(it)
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vgShimmer.setUpShimmerVisibility()
        binding.rv.adapter = adapter
        binding.etSearch.doAfterTextChanged {
            vm.searchFavourites(it?.toString())
        }
        binding.vEmpty.setOnActionBtnClick {
            vm.openHome()
        }
        binding.srl.setOnRefreshListener {
            vm.loadFavourites(true)
        }
        vm.refreshing.observe(viewLifecycleOwner) {
            binding.srl.isRefreshing = it
        }
        vm.favourites.observe(viewLifecycleOwner, ::bindFavourites)
        vm.loading.observe(viewLifecycleOwner) {
            binding.vgShimmer.isVisible = it
            binding.vgContent.isVisible = !it
        }
    }

    private fun bindFavourites(list: List<RecipeDvo>) = with(binding) {
        vgShimmer.isVisible = false
        tvSavedRecipes.text = getString(R.string.favourites_savedRecipes, list.size)
        val isEmpty = list.isEmpty()
        rv.isVisible = !isEmpty
        vEmpty.isVisible = isEmpty
        adapter.submitList(list)
    }
}