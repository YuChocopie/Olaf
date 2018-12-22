package com.mashupgroup.weatherbear.viewmodels

import android.graphics.drawable.Drawable
import com.mashupgroup.weatherbear.R
import com.mashupgroup.weatherbear.WeatherBearApp

class BackgroundViewModel {
    /*

    배경 : 맑음, 흐림
    배경산 : 맑음,흐림
    산의 눈 : 눈, 비, 맑음
    애니매이션... 눈, 비, 구름, 맑음
     */
    enum class Weather {
        SNOW, RAINY, THUNDER_RAINY, WIND, CLOUD, SUNNY, HEAVY_SNOW
    }

    enum class Background(var bg: Drawable, var farMountain: Drawable, var snowVisibility: Boolean) {
        SUNNY(WeatherBearApp.appContext.resources.getDrawable(R.drawable.ic_bg_snunny),
                WeatherBearApp.appContext.resources.getDrawable(R.drawable.bg_sunny_blue), false),
        SNOW(WeatherBearApp.appContext.resources.getDrawable(R.drawable.ic_bg_snow),
                WeatherBearApp.appContext.resources.getDrawable(R.drawable.background_far_mountain), true)
    }

    enum class Snow(var mountainSnow: Drawable, var mountainSnowVisibility: Boolean) {
        SNOW(WeatherBearApp.appContext.resources.getDrawable(R.drawable.ic_msg_bg_mountatin_snow), true),
        RAINY(WeatherBearApp.appContext.resources.getDrawable(R.drawable.ic_msg_bg_mountatin_rainy), false)
    }

    var weather: Weather = Weather.SNOW

    var bg: Drawable = Background.SNOW.bg
    var farMountain: Drawable = Background.SNOW.farMountain
    var snowVisibility: Boolean = Background.SNOW.snowVisibility
    var rainVisibility: Boolean = true
    var cloudVisibility: Boolean = true
    var mountainSnowVisibility: Boolean = Snow.SNOW.mountainSnowVisibility
    var mountainSnow: Drawable = Snow.SNOW.mountainSnow


    fun setBackground() {
        cloudVisibility = false
        rainVisibility = false
        snowVisibility = false
        when (weather) {
            Weather.SUNNY, Weather.WIND, Weather.CLOUD -> {
                bg = Background.SUNNY.bg
                farMountain = Background.SUNNY.farMountain
                snowVisibility = Background.SUNNY.snowVisibility
                if (weather != Weather.SUNNY) {
                    cloudVisibility = true
                }
            }
            else -> {
                bg = Background.SNOW.bg
                farMountain = Background.SNOW.farMountain
                mountainSnowVisibility = Background.SNOW.snowVisibility
                snowVisibility = Background.SNOW.snowVisibility
                if (weather == Weather.THUNDER_RAINY || weather == Weather.RAINY) {
                    mountainSnow = Snow.RAINY.mountainSnow
                    snowVisibility = Background.SUNNY.snowVisibility
                    rainVisibility = true
                } else {
                    mountainSnow = Snow.SNOW.mountainSnow
                }
            }
        }
    }


}