package com.mashupgroup.weatherbear.models.air

data class AirItem(
        /*
        pm10Value   미세먼지 수치
        pm10Value24 오늘미세먼지 수치
        pm25Value 초미세먼지 수치
        pm25Value24 오늘 초미세먼지 수치
        pm10Grade 미세먼지 등급
        pm25Grade 초미세먼지 등급
         */
        var dataTime: String,
        var pm10Value: String,
        var pm10Value24: String,
        var pm25Value: String,
        var pm25Value24: String,
        var pm10Grade: String,
        var pm25Grade: String,
        var pm10Grade1h: String,
        var pm25Grade1h: String
)