package kz.adamant.api

import retrofit2.Converter

interface RetrofitConverterFactory {

    fun getConverterFactory(): Converter.Factory

}