package com.mashupgroup.weatherbear.models.weather

data class Weather(
    /** lon:경도, lat:위도 */
    var coord : Coord,
    /** id:아이디, main:날씨 String, description:설명 String */
    var weather: List<WeatherData>,
    /** Internal parameter */
    var base: String,
    /** temp:온도, humidity:습도%, pressure:대기압,
     *  temp_min:최저 온도, temp_max:최대 온도,
     *  seaLevel:해수면 대기압 hPa, grndLevel:지면위의 대기압 hPa */
    var main: Main,
    /** speed:풍속, deg:풍향 */
    var wind: Wind,
    /** all:흐림% */
    var clouds: Clouds,
    /** 1h:지난 1시간 동안의 강수량, 3h:지난 3시간 동안의 강수량(문제 있음) */
    var rain: Rain,
    /** UTC unix 시간 */
    var dt: Int,
    /** Internal parameter */
    var sys: Sys,
    /** 도시 아이디 */
    var id: Int,
    /** 도시 이름 */
    var name: String,
    /** Internal parameter */
    var cod: Int
)

