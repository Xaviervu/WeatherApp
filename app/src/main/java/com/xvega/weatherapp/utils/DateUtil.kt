package com.xvega.weatherapp.utils

import android.annotation.SuppressLint
import android.content.Context
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@SuppressLint("SimpleDateFormat")
fun getWeekDayByDate(date: String, context: Context): String? {
    val inFormat = SimpleDateFormat("yyyy-MM-dd")
    try {
        val myDate: Date = inFormat.parse(date) ?: return null
        val simpleDateFormat =
            SimpleDateFormat("EEEE", context.resources.configuration.locales.get(0))
        val dayName: String? = simpleDateFormat.format(myDate)
        return dayName
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null
}

@SuppressLint("WeekBasedYear")
fun isTimeMoreOrEqualThanCurrentHour(timeString: String): Boolean {
    val calendarNow = Calendar.getInstance()
    val currentHour = calendarNow.get(Calendar.HOUR_OF_DAY)

    val sdf = SimpleDateFormat("YYYY-MM-dd HH:mm", Locale.getDefault())
    sdf.isLenient = true

    try {
        val parsedDate = sdf.parse(timeString)
        if (parsedDate != null) {
            val calendarInput = Calendar.getInstance()
            calendarInput.time = parsedDate

            val inputHour = calendarInput.get(Calendar.HOUR_OF_DAY)

            return inputHour >= currentHour

        } else {
            return false
        }
    } catch (_: ParseException) {
        return false
    }
}