package com.xvega.weatherapp.model

import com.google.gson.annotations.SerializedName


data class Forecast(
    @SerializedName("forecastday") var forecastDayList: ArrayList<ForecastDay> = ArrayList()
)