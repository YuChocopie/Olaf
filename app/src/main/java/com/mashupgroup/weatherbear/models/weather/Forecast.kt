package com.mashupgroup.weatherbear.models.weather

data class Forecast(
        var cnt : Int,
        var list : List<ForecastWeather>,
        var city : City
)