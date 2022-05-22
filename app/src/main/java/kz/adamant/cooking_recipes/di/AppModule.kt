package kz.adamant.cooking_recipes.di

import android.content.Context
import android.content.SharedPreferences
import kz.adamant.account.AccountViewModel
import kz.adamant.auth.AuthNavigator
import kz.adamant.auth.LoginViewModel
import kz.adamant.auth.ui.AuthViewModel
import kz.adamant.checklist.ui.ChecklistViewModel
import kz.adamant.common.MainNavControllerFinder
import kz.adamant.cooking_recipes.AuthNavigatorImpl
import kz.adamant.cooking_recipes.MainNavControllerFinderImpl
import kz.adamant.cooking_recipes.MainViewModel
import kz.adamant.cooking_recipes.RecipeNavigatorImpl
import kz.adamant.cooking_recipes.SearchNavigatorImpl
import kz.adamant.cooking_recipes.UserPreferencesNavigatorImpl
import kz.adamant.favourites.ui.FavouritesViewModel
import kz.adamant.home.HomeViewModel
import kz.adamant.main.MainFeatureViewModel
import kz.adamant.preferences.UserPreferencesNavigator
import kz.adamant.preferences.UserPreferencesViewModel
import kz.adamant.recipe.RecipeNavigator
import kz.adamant.recipe.ui.models.RecipeDvoToRecipeModelMapper
import kz.adamant.recipe.ui.models.RecipeToRecipeDvoMapper
import kz.adamant.recipe.ui.recipe.RecipeDetailViewModel
import kz.adamant.recipe.ui.recipe.ingredients.RecipeIngredientViewModel
import kz.adamant.recipe.ui.recipe.review.RecipeReviewViewModel
import kz.adamant.recipe.ui.recipe.step.RecipeStepByStepViewModel
import kz.adamant.recipe.ui.recipe.step.timer.RecipeTimerViewModel
import kz.adamant.search.SearchNavigator
import kz.adamant.search.ui.SearchViewModel
import kz.adamant.search.ui.filter.SearchFilterViewModel
import kz.adamant.search.ui.ingredients.SearchIngredientsVIewModel
import kz.adamant.search.ui.result.SearchResultViewModel
import kz.adamant.search.ui.type.SearchByTypeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val SHARED_PREF_NAME = "cookigo_shared_pref"

val appModule = module {

    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            SHARED_PREF_NAME,
            Context.MODE_PRIVATE
        )
    }

    viewModel { AuthViewModel(get(), get()) }
    viewModel { MainViewModel() }
    viewModel { MainFeatureViewModel(get()) }
    viewModel { UserPreferencesViewModel(get(), get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { HomeViewModel(get(), get(), get(), get(), get()) }
    viewModel { AccountViewModel(get()) }
    viewModel { RecipeDetailViewModel(get(), get(), get()) }
    viewModel { RecipeIngredientViewModel(get(), get(named("MAIN"))) }
    viewModel { RecipeStepByStepViewModel(get()) }
    viewModel { parameters -> RecipeTimerViewModel(parameters.get()) }
    viewModel { RecipeReviewViewModel(get()) }
    viewModel { FavouritesViewModel(get(), get(), get(), get()) }
    viewModel { ChecklistViewModel(get(named("MAIN"))) }
    viewModel { SearchViewModel() }
    viewModel { SearchByTypeViewModel(get()) }
    viewModel { SearchResultViewModel(get(), get(), get(), get(), get()) }
    viewModel { SearchFilterViewModel() }
    viewModel { SearchIngredientsVIewModel(get(named("SEARCH"))) }

    factory { RecipeToRecipeDvoMapper() }
    factory { RecipeDvoToRecipeModelMapper() }

    factory<AuthNavigator> { AuthNavigatorImpl() }
    factory<UserPreferencesNavigator> { UserPreferencesNavigatorImpl() }
    factory<RecipeNavigator> { RecipeNavigatorImpl() }
    factory<SearchNavigator> { SearchNavigatorImpl() }
    factory<MainNavControllerFinder> { MainNavControllerFinderImpl() }

}