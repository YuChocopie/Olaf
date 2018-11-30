package com.mashupgroup.weatherbear.models.weather

/*
# Weather Data Class

coord.lon               : 경도
coord.lat               : 위도
weather[0].id           : 날씨 아이디
weather[0].main         : 날씨 String(Ex. Clear)
weather[0].description  : 날씨 설명 String (Ex. clear sky)
weather[0].icon         : 날씨 아이콘 번호
base                    : Internal parameter
main.temp               : Temperature. Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
main.humidity           : 습도 %
main.pressure           : 대기압
main.temp_min           : 순간 최저 온도
main.temp_max           : 순간 최대 온도
main.seaLevel           : 해수면 대기압, hPa
main.grndLevel          : 지면위의 대기압, hPa
wind.speed              : 풍속. 단위 met/sec
wind.deg                : 풍향
clouds.all              : 흐림 %
rain.1h                 : 지난 1시간 동안의 강수량 (문제 있음)
rain.3h                 : 지난 3시간 동안의 강수량 (문제 있음)
dt                      : 시간, unix, UTC
sys.type                : Internal parameter
sys.id                  : Internal parameter
sys.message             : Internal parameter
sys.country             : 나라 코드(Ex. GB, JP)
sys.sunrise             : 일출 시간, unix, UTC
sys.sunset              : 일몰 시간, unix, UTC
id                      : 도시 아이디
name                    : 도시 이름
cod                     : Internal parameter
 */

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