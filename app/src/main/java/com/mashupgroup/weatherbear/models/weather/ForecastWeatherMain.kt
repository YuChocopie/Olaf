package com.mashupgroup.weatherbear.models.weather

data class ForecastWeatherMain(
        var temp: String,
        var temp_min: String,
        var temp_max: String,
        var pressure: String,
        var sea_level: String,
        var grnd_level: String,
        var humidity: String,
        var temp_kf: String
)