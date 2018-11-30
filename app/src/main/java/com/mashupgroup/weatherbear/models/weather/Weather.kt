package com.mashupgroup.weatherbear.models.weather


data class Weather(
    var coord : Coord,
    var weather: List<WeatherData>,
    var base: String,
    var main: Main,
    var wind: Wind,
    var clouds: Clouds,
    var rain: Rain,
    var dt: Int,
    var sys: Sys,
    var id: Int,
    var name: String,
    var cod: Int
)