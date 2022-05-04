package kz.adamant.cooking_recipes.di

import kz.adamant.auth.domain.AuthInteractor
import kz.adamant.checklist.domain.ChecklistInteractor
import kz.adamant.preferences.domain.UserPreferencesInteractor
import kz.adamant.recipe.domain.RecipeInteractor
import kz.adamant.search.domain.SearchInteractor
import org.koin.core.qualifier.named
import org.koin.dsl.module

val domainModule = module {
    factory { AuthInteractor(get()) }
    factory { UserPreferencesInteractor(get()) }
    factory { RecipeInteractor(get(), get(), get()) }
    factory { SearchInteractor(get()) }
    factory<ChecklistInteractor>(named("MAIN")) { ChecklistInteractor.Base(get(named("MAIN"))) }
    factory<ChecklistInteractor>(named("SEARCH")) { ChecklistInteractor.Base(get(named("SEARCH"))) }
}