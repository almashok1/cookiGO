package kz.adamant.cooking_recipes.di

import kz.adamant.checklist.data.ChecklistLocalDataSource
import kz.adamant.checklist.domain.ChecklistHandler
import kz.adamant.data.CacheCleaner
import kz.adamant.data.CacheCleaners
import kz.adamant.recipe.data.RecipeLocalDataSource
import kz.adamant.search.data.SearchIngredientsLocalDataSource
import org.koin.core.qualifier.named
import org.koin.dsl.binds
import org.koin.dsl.module

val dataModule = module {
    single { RecipeLocalDataSource() }
    single(named("MAIN")) { ChecklistLocalDataSource(get(), get()) } binds arrayOf(
        ChecklistHandler::class, CacheCleaner::class
    )
    single<ChecklistHandler>(named("SEARCH")) { SearchIngredientsLocalDataSource() }

    single {
        CacheCleaners(
            listOf(get(named("MAIN")))
        )
    }
}