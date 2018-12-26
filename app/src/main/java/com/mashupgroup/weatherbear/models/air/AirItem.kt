package com.mashupgroup.weatherbear.models.air

data class AirItem(
        var dataTime: String,
        var pm10Value: String,
        var pm10Value24: String,
        var pm25Value: String,
        var pm25Value24: String,
        var pm10Grade: String,
        var pm25Grade: String,
        var pm10Grade1h: String,
        var pm25Grade1h: String
)