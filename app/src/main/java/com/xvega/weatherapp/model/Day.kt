package com.xvega.weatherapp.model

import com.google.gson.annotations.SerializedName


data class Day(
    @SerializedName("maxtemp_c") var maxtempC: Double = 0.0,
    @SerializedName("maxtemp_f") var maxtempF: Double = 0.0,
    @SerializedName("mintemp_c") var mintempC: Double = 0.0,
    @SerializedName("mintemp_f") var mintempF: Double = 0.0,
    @SerializedName("avgtemp_c") var avgtempC: Double = 0.0,
    @SerializedName("avgtemp_f") var avgtempF: Double = 0.0,
    @SerializedName("maxwind_mph") var maxwindMph: Double = 0.0,
    @SerializedName("maxwind_kph") var maxwindKph: Double = 0.0,
    @SerializedName("totalprecip_mm") var totalprecipMm: Double = 0.0,
    @SerializedName("totalprecip_in") var totalprecipIn: Double = 0.0,
    @SerializedName("totalsnow_cm") var totalsnowCm: Double = 0.0,
    @SerializedName("avgvis_km") var avgvisKm: Double = 0.0,
    @SerializedName("avgvis_miles") var avgvisMiles: Double = 0.0,
    @SerializedName("avghumidity") var avghumidity: Int = 0,
    @SerializedName("daily_will_it_rain") var dailyWillItRain: Int = 0,
    @SerializedName("daily_chance_of_rain") var dailyChanceOfRain: Int = 0,
    @SerializedName("daily_will_it_snow") var dailyWillItSnow: Int = 0,
    @SerializedName("daily_chance_of_snow") var dailyChanceOfSnow: Int = 0,
    @SerializedName("condition") var condition: Condition = Condition(),
    @SerializedName("uv") var uv: Double = 0.0
)