package com.mashupgroup.weatherbear.viewmodels

import android.graphics.drawable.Drawable
import android.util.Log
import com.mashupgroup.weatherbear.R
import com.mashupgroup.weatherbear.WeatherBearApp
import java.util.*


class IsDayViewModel {

    //SNOW, RAINY, THUNDER_RAINY, WIND, CLOUD, SUNNY
    var todayWeatherData = "CLOUD"
    var todayDustLevelData = 2
    var todayUltraDustLevelData = 2
    var todayTemperatureData = "3"
    var todayBodyTemperatureData = "1"
    var todayDustData = "100up"
    var todayUltraDustData = "100up"
    var todayTime: Boolean = true

    var tomorrowWetherTextData = "오늘보다 선선해요"
    var tomorrowWeatherData = "RAIN"
    var tomorrowTemperatureData = "3"
    var tomorrowBodyTemperatureData = "1"
    var tomorrowDustLevelData = 2
    var tomorrowUltraDustLevelData = 1
    var tomorrowDustData = "100up"
    var tomorrowUltraDustData = "100up"

    /*
    날씨 :Image, text
    현재온도 tomorrowTemperature
    체감온도 body_temperature
    미세먼지 fine_dust
    미세먼지 fine_dust_level
    초미세먼지 ultrafine_dust
    초미세먼지 ultrafine_dust_lavel
     */

    enum class Weather(var weatherText: String, var image: Drawable, var text: String) {
        SNOW("SNOW",
                WeatherBearApp.appContext.resources.getDrawable(R.drawable.msg_snow),
                WeatherBearApp.appContext.resources.getString(R.string.msg_snow)),
        HEAVY_SNOW("HEAVY_SNOW",
                WeatherBearApp.appContext.resources.getDrawable(R.drawable.msg_snow),
                WeatherBearApp.appContext.resources.getString(R.string.msg_heavy_snow)),
        WIND("WIND",
                WeatherBearApp.appContext.resources.getDrawable(R.drawable.msg_wind),
                WeatherBearApp.appContext.resources.getString(R.string.msg_wind)),
        SUNNY("SUNNY",
                WeatherBearApp.appContext.resources.getDrawable(R.drawable.msg_sunny),
                WeatherBearApp.appContext.resources.getString(R.string.msg_sunny)),
        SUNNY_NIGHT("SUNNY_NIGHT",
                WeatherBearApp.appContext.resources.getDrawable(R.drawable.msg_sunny_night),
                WeatherBearApp.appContext.resources.getString(R.string.msg_sunny_night)),
        CLOUD("CLOUD",
                WeatherBearApp.appContext.resources.getDrawable(R.drawable.msg_cloud),
                WeatherBearApp.appContext.resources.getString(R.string.msg_cloud)),
        CLOUD_NIGHT("CLOUD_NIGHT",
                WeatherBearApp.appContext.resources.getDrawable(R.drawable.msg_cloudy_night),
                WeatherBearApp.appContext.resources.getString(R.string.msg_cloud_night)),
        RAINY("RAINY",
                WeatherBearApp.appContext.resources.getDrawable(R.drawable.msg_rainy),
                WeatherBearApp.appContext.resources.getString(R.string.msg_rain)),
        THUNDER("THUNDER",
                WeatherBearApp.appContext.resources.getDrawable(R.drawable.msg_thunder),
                WeatherBearApp.appContext.resources.getString(R.string.msg_thunder)),
        THUNDER_RAINY("THUNDER_RAINY",
                WeatherBearApp.appContext.resources.getDrawable(R.drawable
                        .msg_thunder_rainy),
                WeatherBearApp.appContext.resources.getString(R.string.msg_thunder_rainy)),
    }

    enum class DustLevel(var level: Int, var color: Int, var text: String) {
        GOOD(1, WeatherBearApp.appContext.resources.getColor(R.color.fineDustGood)
                , WeatherBearApp.appContext.resources.getString(R.string.fine_dust_good)),
        NOMAL(2, WeatherBearApp.appContext.resources.getColor(R.color.fineDustNormal)
                , WeatherBearApp.appContext.resources.getString(R.string.fine_dust_normal)),
        BAD(3, WeatherBearApp.appContext.resources.getColor(R.color.fineDustBad)
                , WeatherBearApp.appContext.resources.getString(R.string.fine_dust_bad)),
        VERYBAD(4, WeatherBearApp.appContext.resources.getColor(R.color.fineDustVeryBad)
                , WeatherBearApp.appContext.resources.getString(R.string.fine_dust_very_bad))
    }


    var todayWeatherShape = Weather.SNOW.image
    var todayWeatherShapeTint = WeatherBearApp.appContext.resources.getColor(R.color.Snow)
    var todayWeatherText = Weather.SNOW.text
    var todayTemperature = "3"
    var todayBodyTemperature = "1"
    var todayDustText = DustLevel.BAD.text
    var todayDustColor = DustLevel.BAD.color
    var todayDust = "100up"
    var todayUltraDust = "100up"
    var todayUltraDustText = DustLevel.BAD.text
    var todayUltraDustColor = DustLevel.BAD.color

    var tomorrowWetherText = "오늘보다 선선해요"
    var tomorrowWeatherShapeTint = WeatherBearApp.appContext.resources.getColor(R.color.Snow)
    var tomorrowWeatherShape = Weather.SNOW.image
    var tomorrowWeatherText = Weather.SNOW.text
    var tomorrowTemperature = "3"
    var tomorrowBodyTemperature = "1"
    var tomorrowDust = "100up"
    var tomorrowDustText = DustLevel.BAD.text
    var tomorrowDustColor = DustLevel.BAD.color
    var tomorrowUltraDust = "100up"
    var tomorrowUltraDustText = DustLevel.BAD.text
    var tomorrowUltraDustColor = DustLevel.BAD.color


    fun setDayView() {
        getTime()
        todayTemperature = todayTemperatureData
        todayBodyTemperature = todayBodyTemperatureData
        todayDust = todayDustData
        todayUltraDust = todayUltraDustData

        tomorrowWetherText = tomorrowWetherTextData
        tomorrowTemperature = tomorrowTemperatureData
        tomorrowBodyTemperature = tomorrowBodyTemperatureData
        tomorrowDust = tomorrowDustData
        tomorrowUltraDust = tomorrowUltraDustData

        setTodayWeather()
        setTomorrowWeather()

    }

    private fun getTime() {
        val date = Date()
        val hTime = (date.hours)
        todayTime = hTime in 6..17
        Log.e("htime", hTime.toString())
        Log.e("htime", todayTime.toString())
    }

    fun setTodayWeather() {
        if (!todayTime) {
            when (todayWeatherData) {
                Weather.CLOUD.weatherText -> {
                    todayWeatherData = Weather.CLOUD_NIGHT.weatherText
                }
            }
            when (todayWeatherData) {
                Weather.SUNNY.weatherText -> {
                    todayWeatherData = Weather.SUNNY_NIGHT.weatherText
                }
            }
        }


        when (todayWeatherData) {
            Weather.SNOW.weatherText -> {
                todayWeatherShape = Weather.SNOW.image
                todayWeatherText = Weather.SNOW.text
            }
            Weather.HEAVY_SNOW.weatherText -> {
                todayWeatherShape = Weather.HEAVY_SNOW.image
                todayWeatherText = Weather.HEAVY_SNOW.text
                todayWeatherShapeTint = WeatherBearApp.appContext.resources.getColor(R.color
                        .heavySnow)
            }
            Weather.RAINY.weatherText -> {
                todayWeatherShape = Weather.RAINY.image
                todayWeatherText = Weather.RAINY.text
            }
            Weather.THUNDER_RAINY.weatherText -> {
                todayWeatherShape = Weather.THUNDER_RAINY.image
                todayWeatherText = Weather.THUNDER_RAINY.text
            }
            Weather.WIND.weatherText -> {
                todayWeatherShape = Weather.WIND.image
                todayWeatherText = Weather.WIND.text
            }
            Weather.CLOUD.weatherText -> {
                todayWeatherShape = Weather.CLOUD.image
                todayWeatherText = Weather.CLOUD.text
            }
            Weather.CLOUD_NIGHT.weatherText -> {
                todayWeatherShape = Weather.CLOUD_NIGHT.image
                todayWeatherText = Weather.CLOUD_NIGHT.text
            }
            Weather.SUNNY.weatherText -> {
                todayWeatherShape = Weather.SUNNY.image
                todayWeatherText = Weather.SUNNY.text
            }
            Weather.SUNNY_NIGHT.weatherText -> {
                todayWeatherShape = Weather.SUNNY_NIGHT.image
                todayWeatherText = Weather.SUNNY_NIGHT.text
            }
        }
        when (todayDustLevelData) {
            DustLevel.GOOD.level -> {
                todayDustText = DustLevel.GOOD.text
                todayDustColor = DustLevel.GOOD.color
            }
            DustLevel.NOMAL.level -> {
                todayDustText = DustLevel.NOMAL.text
                todayDustColor = DustLevel.NOMAL.color
            }
            DustLevel.BAD.level -> {
                todayDustText = DustLevel.BAD.text
                todayDustColor = DustLevel.BAD.color
            }
            DustLevel.VERYBAD.level -> {
                todayDustText = DustLevel.VERYBAD.text
                todayDustColor = DustLevel.VERYBAD.color
            }
        }
        when (todayUltraDustLevelData) {
            DustLevel.GOOD.level -> {
                todayUltraDustText = DustLevel.GOOD.text
                todayUltraDustColor = DustLevel.GOOD.color
            }
            DustLevel.NOMAL.level -> {
                todayUltraDustText = DustLevel.NOMAL.text
                todayUltraDustColor = DustLevel.NOMAL.color
            }
            DustLevel.BAD.level -> {
                todayUltraDustText = DustLevel.BAD.text
                todayUltraDustColor = DustLevel.BAD.color
            }
            DustLevel.VERYBAD.level -> {
                todayUltraDustText = DustLevel.VERYBAD.text
                todayUltraDustColor = DustLevel.VERYBAD.color
            }
        }
    }

    fun setTomorrowWeather() {
        when (tomorrowWeatherData) {
            Weather.SNOW.weatherText -> {
                tomorrowWeatherShape = Weather.SNOW.image
                tomorrowWeatherText = Weather.SNOW.text
            }
            Weather.HEAVY_SNOW.weatherText -> {
                tomorrowWeatherShape = Weather.HEAVY_SNOW.image
                tomorrowWeatherText = Weather.HEAVY_SNOW.text
                tomorrowWeatherShapeTint = WeatherBearApp.appContext.resources.getColor(R.color
                        .heavySnow)
            }
            Weather.RAINY.weatherText -> {
                tomorrowWeatherShape = Weather.RAINY.image
                tomorrowWeatherText = Weather.RAINY.text
            }
            Weather.THUNDER_RAINY.weatherText -> {
                tomorrowWeatherShape = Weather.THUNDER_RAINY.image
                tomorrowWeatherText = Weather.THUNDER_RAINY.text
            }
            Weather.WIND.weatherText -> {
                tomorrowWeatherShape = Weather.WIND.image
                tomorrowWeatherText = Weather.WIND.text
            }
            Weather.CLOUD.weatherText -> {
                tomorrowWeatherShape = Weather.CLOUD.image
                tomorrowWeatherText = Weather.CLOUD.text
            }
            Weather.CLOUD_NIGHT.weatherText -> {
                tomorrowWeatherShape = Weather.CLOUD_NIGHT.image
                tomorrowWeatherText = Weather.CLOUD_NIGHT.text
            }
            Weather.SUNNY.weatherText -> {
                tomorrowWeatherShape = Weather.SUNNY.image
                tomorrowWeatherText = Weather.SUNNY.text
            }
            Weather.SUNNY_NIGHT.weatherText -> {
                tomorrowWeatherShape = Weather.SNOW.image
                tomorrowWeatherText = Weather.SUNNY_NIGHT.text
            }
        }
        when (tomorrowDustLevelData) {
            DustLevel.GOOD.level -> {
                tomorrowDustText = DustLevel.GOOD.text
                tomorrowDustColor = DustLevel.GOOD.color
            }
            DustLevel.NOMAL.level -> {
                tomorrowDustText = DustLevel.NOMAL.text
                tomorrowDustColor = DustLevel.NOMAL.color
            }
            DustLevel.BAD.level -> {
                tomorrowDustText = DustLevel.BAD.text
                tomorrowDustColor = DustLevel.BAD.color
            }
            DustLevel.VERYBAD.level -> {
                tomorrowDustText = DustLevel.VERYBAD.text
                tomorrowDustColor = DustLevel.VERYBAD.color
            }
        }
        when (tomorrowUltraDustLevelData) {
            DustLevel.GOOD.level -> {
                tomorrowUltraDustText = DustLevel.GOOD.text
                tomorrowUltraDustColor = DustLevel.GOOD.color
            }
            DustLevel.NOMAL.level -> {
                tomorrowUltraDustText = DustLevel.NOMAL.text
                tomorrowUltraDustColor = DustLevel.NOMAL.color
            }
            DustLevel.BAD.level -> {
                tomorrowUltraDustText = DustLevel.BAD.text
                tomorrowUltraDustColor = DustLevel.BAD.color
            }
            DustLevel.VERYBAD.level -> {
                tomorrowUltraDustText = DustLevel.VERYBAD.text
                tomorrowUltraDustColor = DustLevel.VERYBAD.color
            }
        }
    }


    //데이터가 없을 경우
    private fun checkData() {
        //TODO::대처
    }
}