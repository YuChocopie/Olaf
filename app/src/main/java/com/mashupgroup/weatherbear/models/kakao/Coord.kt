package com.mashupgroup.weatherbear.models.kakao

data class Coord(
        var meta: Meta,
        var documents: List<Documents>
)

data class Documents(
        var x: String,
        var y: String
)

data class Meta(
        var total_count: Int
)