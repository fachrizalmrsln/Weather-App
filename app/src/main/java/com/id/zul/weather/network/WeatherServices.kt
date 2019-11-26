package com.id.zul.weather.network

import com.id.zul.weather.model.ForecastResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherServices {

    @GET("forecast/")
    fun getCityWeather(
        @Query("q") city: String,
        @Query("appid") Network: String
    ): Call<ForecastResponse>

}