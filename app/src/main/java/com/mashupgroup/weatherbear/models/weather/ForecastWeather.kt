package com.mashupgroup.weatherbear.models.weather

data class ForecastWeather(
        var dt: String,
        var main: ForecastWeatherMain,
        var weather: List<WeatherData>,
        var wind: WindData
)