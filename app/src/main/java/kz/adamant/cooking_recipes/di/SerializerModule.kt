package kz.adamant.cooking_recipes.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kz.adamant.api.DateAdapter
import kz.adamant.api.MoshiSerializer
import kz.adamant.api.RetrofitConverterFactory
import kz.adamant.arch.api.JsonSerializer
import org.koin.dsl.module

val serializerModule = module {

    single<Moshi> { Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(DateAdapter())
        .build()
    }

    single<JsonSerializer> { MoshiSerializer(get())  }

    single<RetrofitConverterFactory> { MoshiSerializer(get())  }

}