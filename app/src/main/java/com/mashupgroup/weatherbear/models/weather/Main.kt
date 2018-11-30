package com.mashupgroup.weatherbear.models.weather

data class Main (
        var temp: Double,
        var pressure: Double,
        var humidity: Int,
        var tempMin: Double,
        var tempMax: Double,
        var seaLevel: Double,
        var grndLevel: Double
)