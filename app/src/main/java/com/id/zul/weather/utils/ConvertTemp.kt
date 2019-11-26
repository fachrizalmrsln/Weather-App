package com.id.zul.weather.utils

import android.util.Log
import java.text.DecimalFormat

class ConvertTemp {

    fun kelvinToCelsius(data: Double): Int {
        val kelvin: Int
        Log.d("Test", data.toString())
        kelvin = if (data.toString().length > 2) {
            val removeDecimal = DecimalFormat("#")
            removeDecimal.format(data).toInt()
        } else
            data.toInt()
        val fahrenheit = 273
        return kelvin - fahrenheit
    }

}