package com.xvega.weatherapp.api

import com.xvega.weatherapp.api.Connection.KEY
import com.xvega.weatherapp.model.WeatherData
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAppInterface {

    @GET(Connection.DATA_LIST)
    suspend fun getWeatherData(
        @Query("key") key: String = KEY,
        @Query("q") location: String,
        @Query("days") daysN: Int
    ): WeatherData
}