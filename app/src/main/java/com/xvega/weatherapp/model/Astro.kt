package com.xvega.weatherapp.model

import com.google.gson.annotations.SerializedName


data class Astro(
    @SerializedName("sunrise") var sunrise: String = "",
    @SerializedName("sunset") var sunset: String = "",
    @SerializedName("moonrise") var moonrise: String = "",
    @SerializedName("moonset") var moonset: String = "",
    @SerializedName("moon_phase") var moonPhase: String = "",
    @SerializedName("moon_illumination") var moonIllumination: Int = 0,
    @SerializedName("is_moon_up") var isMoonUp: Int = 0,
    @SerializedName("is_sun_up") var isSunUp: Int = 0
)