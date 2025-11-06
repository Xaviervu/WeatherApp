package com.xvega.weatherapp.api

object Connection {
    //"https://api.weatherapi.com/v1/forecast.json?key=fa8b3df74d4042b9aa7135114252304&q=55.7569,37.6151&days=3"
    const val BASE_URL = "https://api.weatherapi.com/"
    private const val V1 = "v1/"
    const val DATA_LIST =
        V1 + "forecast.json"//?key=fa8b3df74d4042b9aa7135114252304&q=55.7569,37.6151&days=3"
    const val KEY = "fa8b3df74d4042b9aa7135114252304"
}