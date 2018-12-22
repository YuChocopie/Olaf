package com.mashupgroup.weatherbear.viewmodel

import android.graphics.drawable.Drawable
import com.mashupgroup.weatherbear.R
import com.mashupgroup.weatherbear.WeatherBearApp

class LocalViewModel {
    enum class WeatherShape(var image: Drawable) {
        SNOW(WeatherBearApp.appContext.resources.getDrawable(R.drawable.msg_snow)),
        WIND(WeatherBearApp.appContext.resources.getDrawable(R.drawable.msg_wind)),
        SUNNY(WeatherBearApp.appContext.resources.getDrawable(R.drawable.msg_sunny)),
        SUNNYNIGHT(WeatherBearApp.appContext.resources.getDrawable(R.drawable.msg_sunny_night)),
        CLOUD(WeatherBearApp.appContext.resources.getDrawable(R.drawable.msg_cloud)),
        CLOUDNIGHT(WeatherBearApp.appContext.resources.getDrawable(R.drawable.msg_cloudy_night)),
        RAINY(WeatherBearApp.appContext.resources.getDrawable(R.drawable.msg_rainy)),
        THUNDER(WeatherBearApp.appContext.resources.getDrawable(R.drawable.msg_thunder)),
        THUNDER_RAINY(WeatherBearApp.appContext.resources.getDrawable(R.drawable.msg_thunder_rainy))
    }

    enum class FineDustLevel(var color: Int, var text: String) {
        GOOD(WeatherBearApp.appContext.resources.getColor(R.color.fineDustGood)
                , WeatherBearApp.appContext.resources.getString(R.string.fine_dust_good)),
        NOMAL(WeatherBearApp.appContext.resources.getColor(R.color.fineDustNormal)
                , WeatherBearApp.appContext.resources.getString(R.string.fine_dust_normal)),
        BAD(WeatherBearApp.appContext.resources.getColor(R.color.fineDustBad)
                , WeatherBearApp.appContext.resources.getString(R.string.fine_dust_bad)),
        VERYBAD(WeatherBearApp.appContext.resources.getColor(R.color.fineDustVeryBad)
                , WeatherBearApp.appContext.resources.getString(R.string.fine_dust_very_bad))
    }

    var localTime = "오전 5:33"
    var localName = "강남구"
    var localWeatherShape = WeatherShape.CLOUD.image
    var localTemperature = "-3"
    var localFineDust="100up"
    var localFineDustColor = FineDustLevel.BAD.color
    var localFineDustText = FineDustLevel.BAD.text
}