package com.id.zul.weather.network

import com.id.zul.weather.utils.Network
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherClient {
    fun getClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Network.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}