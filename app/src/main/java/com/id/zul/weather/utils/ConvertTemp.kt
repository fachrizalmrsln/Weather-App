package com.id.zul.weather.utils

import java.text.DecimalFormat

class ConvertTemp {

    fun kelvinToCelsius(data: Double): String {
        val kelvin: Int
        kelvin = if (data.toString().length > 2) {
            val removeDecimal = DecimalFormat("#")
            removeDecimal.format(data).toInt()
        } else
            data.toInt()
        val fahrenheit = 273
        val result = kelvin - fahrenheit
        return result.toString()
    }

}