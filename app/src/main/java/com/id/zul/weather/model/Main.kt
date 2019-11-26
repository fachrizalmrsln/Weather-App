package com.id.zul.weather.model

data class Main(
    val grnd_level: Int,
    val humidity: Int,
    val pressure: Int,
    val sea_level: Int,
    val temp: Double,
    val temp_kf: String,
    val temp_max: String,
    val temp_min: Double
)