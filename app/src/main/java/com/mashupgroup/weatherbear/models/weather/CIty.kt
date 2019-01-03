package com.mashupgroup.weatherbear.models.weather

data class  City(
        var id: String,
        var name: String,
        var coord: Coord,
        var country: String
)