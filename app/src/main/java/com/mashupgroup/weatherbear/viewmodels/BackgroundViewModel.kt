package com.mashupgroup.weatherbear.viewmodels

import android.graphics.drawable.Drawable
import com.mashupgroup.weatherbear.R
import com.mashupgroup.weatherbear.WeatherBearApp

class BackgroundViewModel {

    var weatherData = "WIND"

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

    fun setBackground() {
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
}