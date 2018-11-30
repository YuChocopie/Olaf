package com.mashupgroup.weatherbear.models.weather

data class Sys(
        var type: Int,
        var id: Int,
        var message: Double,
        var country: String,
        var sunrise: Int,
        var sunset: Int
)