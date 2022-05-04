package kz.adamant.cooking_recipes.di

import com.chuckerteam.chucker.api.ChuckerInterceptor
import kz.adamant.api.ApiBuilder
import kz.adamant.auth.data.AuthApi
import kz.adamant.data.api.AuthInterceptor
import kz.adamant.data.api.BASE_URL_MAIN
import kz.adamant.data.api.BASE_URL_SECONDARY
import kz.adamant.data.api.RefreshApi
import kz.adamant.preferences.data.UserPreferencesApi
import kz.adamant.recipe.data.RecipeApi
import kz.adamant.recipe.data.RecipeSecondaryApi
import kz.adamant.search.data.SearchApi
import org.koin.dsl.module

val networkModule = module {
    single { AuthInterceptor(get()) }
    single { ChuckerInterceptor.Builder(get()).build() }

    single<AuthApi> {
        ApiBuilder(get(), get()).buildService(BASE_URL_MAIN, get<ChuckerInterceptor>())
    }

    single<RefreshApi> {
        ApiBuilder(get(), get()).buildService(BASE_URL_MAIN, get<ChuckerInterceptor>())
    }

    single<UserPreferencesApi> {
        ApiBuilder(get(), get()).buildService(BASE_URL_MAIN, get<ChuckerInterceptor>(), get<AuthInterceptor>())
    }

    single<RecipeApi> {
        ApiBuilder(get(), get()).buildService(BASE_URL_MAIN, get<ChuckerInterceptor>(), get<AuthInterceptor>())
    }

    single<RecipeSecondaryApi> {
        ApiBuilder(get(), get()).buildService(BASE_URL_SECONDARY, get<ChuckerInterceptor>(), get<AuthInterceptor>())
    }

    single<SearchApi> {
        ApiBuilder(get(), get()).buildService(BASE_URL_MAIN, get<ChuckerInterceptor>(), get<AuthInterceptor>())
    }
}