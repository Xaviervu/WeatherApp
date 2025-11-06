package com.xvega.weatherapp.model

import com.google.gson.annotations.SerializedName


data class Location(
    @SerializedName("name") var name: String = "",
    @SerializedName("region") var region: String = "",
    @SerializedName("country") var country: String = "",
    @SerializedName("lat") var lat: Double = 0.0,
    @SerializedName("lon") var lon: Double = 0.0,
    @SerializedName("tz_id") var tzId: String = "",
    @SerializedName("localtime_epoch") var localtimeEpoch: Int = 0,
    @SerializedName("localtime") var localtime: String = ""
)