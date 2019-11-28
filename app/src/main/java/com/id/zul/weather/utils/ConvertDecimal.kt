package com.id.zul.weather.utils

import java.text.DecimalFormat

class ConvertDecimal {

    fun removeDecimal(data: Int): String {
        return DecimalFormat("#").format(data)
    }
}