package com.mashupgroup.weatherbear.models.weather

data class WeatherData (
        var id: Int,
        var main: String,
        var description: String,
        var icon: String
)