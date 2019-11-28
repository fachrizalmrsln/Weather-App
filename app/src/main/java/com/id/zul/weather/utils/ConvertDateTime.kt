package com.id.zul.weather.utils

import java.text.SimpleDateFormat
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ConvertDateTime {

    private val fullFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH", Locale.getDefault())
    private val dateMonthFormat = SimpleDateFormat("MMMM dd", Locale.getDefault())

    fun convertWithoutToday(data: String): String {
        val date = fullFormat.parse(data)

        val expectedFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        val dateToday = SimpleDateFormat("dd", Locale.getDefault())

        val valueToday = dateToday.format(getInstanceTomorrow())

        return if (dateToday.format(date) == valueToday) {
            "Tomorrow"
        } else
            expectedFormat.format(date)
    }

    fun convertToday(): String {
        val date = dateFormat.parse(getToday())
        return "Today, " + dateMonthFormat.format(date)
    }

    fun convertDateMonth(data: String): String {
        val date = dateFormat.parse(data)
        return dateMonthFormat.format(date)
    }

    fun convertTime(data: String): Int {
        val time = fullFormat.parse(data)
        return timeFormat.format(time).toInt()
    }

    fun getToday(): String {
        return dateFormat.format(getInstanceToday())
    }

    fun getCurrentTime(): Int {
        val currentTime = timeFormat.format(getInstanceCurrentTime())
        return currentTime.toInt()
    }

    private fun getInstanceToday(): Date {
        val calendar = Calendar.getInstance()
        return calendar.time
    }

    private fun getInstanceTomorrow(): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        return calendar.time
    }

    private fun getInstanceCurrentTime(): Date {
        val calendar = Calendar.getInstance()
        return calendar.time
    }

}