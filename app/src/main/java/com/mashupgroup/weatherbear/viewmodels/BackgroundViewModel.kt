package com.mashupgroup.weatherbear.viewmodels

import android.graphics.drawable.Drawable
import com.mashupgroup.weatherbear.R
import com.mashupgroup.weatherbear.WeatherBearApp

class BackgroundViewModel {

    var weatherData = "WIND"
    var fineDustLevel = 2
    var tvMainWeatherMessage = "오늘은 날씨가 좋아요"

    /*
    배경 : 맑음, 흐림
    배경산 : 맑음,흐림
    산의 눈 : 눈, 비, 맑음
    애니매이션... 눈, 비, 구름, 맑음
     */

    enum class Weather(var text: String) {
        SNOW("SNOW"), RAINY("RAINY"), THUNDER_RAINY("THUNDER_RAINY"),
        WIND("WIND"), CLOUD("CLOUD"), SUNNY("SUNNY"), HEAVY_SNOW("HEAVY_SNOW")
    }

    enum class Background(var bg: Drawable, var farMountain: Drawable, var snowVisibility: Boolean) {
        SUNNY(WeatherBearApp.appContext.resources.getDrawable(R.drawable.ic_bg_snunny),
                WeatherBearApp.appContext.resources.getDrawable(R.drawable.ic_bg_sunny_blue), false),
        SNOW(WeatherBearApp.appContext.resources.getDrawable(R.drawable.ic_bg_snow),
                WeatherBearApp.appContext.resources.getDrawable(R.drawable.ic_bg_snow_blue),
                true)
    }

    enum class Snow(var mountainSnow: Drawable) {
        SNOW(WeatherBearApp.appContext.resources.getDrawable(R.drawable.ic_msg_bg_mountatin_snow)),
        RAINY(WeatherBearApp.appContext.resources.getDrawable(R.drawable.ic_msg_bg_mountatin_rainy))
    }

    var weather = Weather.SNOW.text
    var bg: Drawable = Background.SNOW.bg
    var farMountain: Drawable = Background.SNOW.farMountain
    var snowVisibility: Boolean = Background.SUNNY.snowVisibility
    var rainVisibility: Boolean = false
    var cloudVisibility: Boolean = false
    var mountainSnowVisibility: Boolean = true
    var mountainSnow: Drawable = Snow.SNOW.mountainSnow
    var message = " "

    fun setBackground() {
        setWeatherMessage()
        tvMainWeatherMessage = message
        cloudVisibility = false
        rainVisibility = false
        snowVisibility = false
        when (weatherData) {
            Weather.SUNNY.text, Weather.WIND.text, Weather.CLOUD.text -> {
                bg = Background.SUNNY.bg
                farMountain = Background.SUNNY.farMountain
                snowVisibility = Background.SUNNY.snowVisibility
                mountainSnowVisibility = false
                if (weatherData.equals(Weather.WIND.text) || weatherData.equals(Weather.CLOUD
                                .text)) {
                    cloudVisibility = true
                }
            }
            else -> {
                bg = Background.SNOW.bg
                farMountain = Background.SNOW.farMountain
                mountainSnow = Snow.RAINY.mountainSnow
                mountainSnowVisibility = true
                if (weatherData.equals(Weather.THUNDER_RAINY.text) || weatherData.equals(Weather.RAINY
                                .text)) {
                    mountainSnow = Snow.RAINY.mountainSnow
                    snowVisibility = Background.SUNNY.snowVisibility
                    rainVisibility = true
                } else {
                    snowVisibility = Background.SNOW.snowVisibility
                    mountainSnow = Snow.SNOW.mountainSnow
                }
            }
        }
    }

    private fun setWeatherMessage() {
        val weatherMsg: String = when (weatherData) {
            Weather.SNOW.text -> WeatherBearApp.appContext.resources.getString(R.string.msg_top_snowy)
            Weather.HEAVY_SNOW.text -> WeatherBearApp.appContext.resources.getString(R.string.msg_top_snowy)
            Weather.RAINY.text -> WeatherBearApp.appContext.resources.getString(R.string.msg_top_rainy)
            Weather.THUNDER_RAINY.text -> WeatherBearApp.appContext.resources.getString(R.string.msg_top_thunder)
            Weather.SUNNY.text -> WeatherBearApp.appContext.resources.getString(R.string.msg_top_sunny)
            Weather.CLOUD.text -> WeatherBearApp.appContext.resources.getString(R.string.msg_top_cloudy)
            else -> WeatherBearApp.appContext.resources.getString(R.string.msg_top_unknown)
        }
        val airMsg: String = when (fineDustLevel) {
            1 -> WeatherBearApp.appContext.resources.getString(R.string.msg_top_air_good)
            2 -> WeatherBearApp.appContext.resources.getString(R.string.msg_top_air_normal)
            3 -> WeatherBearApp.appContext.resources.getString(R.string.msg_top_air_bad)
            4 -> WeatherBearApp.appContext.resources.getString(R.string.msg_top_air_worst)
            else -> WeatherBearApp.appContext.resources.getString(R.string.msg_top_unknown)
        }

        message = String.format(WeatherBearApp.appContext.resources.getString(R.string.msg_top_weather_ps_air_quality_ps), weatherMsg, airMsg)
    }
}