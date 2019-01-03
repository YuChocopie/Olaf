package com.mashupgroup.weatherbear.models.weather

data class ForecastWeather(
        var dt: String,
        var forecastWeatherMain : ForecastWeatherMain,
        var weather: List<WeatherData>
)