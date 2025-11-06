package com.xvega.weatherapp.repository

import RetrofitClient
import com.xvega.weatherapp.model.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

object DataRepository {

    fun getWeatherData(location: String, daysN: Int): Flow<WeatherData> = flow {
        val r = RetrofitClient.retrofit.getWeatherData(location = location, daysN = daysN)
        emit(r)
    }.flowOn(Dispatchers.IO)

}
