package com.mashupgroup.weatherbear

import android.arch.lifecycle.ViewModel
import android.graphics.drawable.Drawable


class IsDayViewModel : ViewModel() {
    /*
        날씨 :Image, text
        현재온도 tomorrowTemperature
        체감온도 body_temperature
        미세먼지 fine_dust
        미세먼지 fine_dust_level
        초미세먼지 ultrafine_dust
        초미세먼지 ultrafine_dust_lavel
         */

    enum class Weather(var image: Drawable, var text: String) {
        SNOW(WeatherBearApp.appContext.resources.getDrawable(R.drawable.msg_snow), WeatherBearApp
                .appContext.resources.getString(R.string.msg_snow)),
        WIND(WeatherBearApp.appContext.resources.getDrawable(R.drawable.msg_wind), WeatherBearApp
                .appContext.resources.getString(R.string.msg_wind)),
        SUNNY(WeatherBearApp.appContext.resources.getDrawable(R.drawable.msg_sunny), WeatherBearApp
                .appContext.resources.getString(R.string.msg_sunny)),
        SUNNYNIGHT(WeatherBearApp.appContext.resources.getDrawable(R.drawable.msg_sunny_night),
                WeatherBearApp.appContext.resources.getString(R.string.msg_sunny_night)),
        CLOUD(WeatherBearApp.appContext.resources.getDrawable(R.drawable.msg_cloud), WeatherBearApp
                .appContext.resources.getString(R.string.msg_cloud)),
        CLOUDNIGHT(WeatherBearApp.appContext.resources.getDrawable(R.drawable.msg_cloudy_night),
                WeatherBearApp.appContext.resources.getString(R.string.msg_cloud_night)),
        RAINY(WeatherBearApp.appContext.resources.getDrawable(R.drawable.msg_rainy), WeatherBearApp
                .appContext.resources.getString(R.string.msg_rainy)),
        THUNDER(WeatherBearApp.appContext.resources.getDrawable(R.drawable.msg_thunder),
                WeatherBearApp.appContext.resources.getString(R.string.msg_thunder)),
        THUNDER_RAINY(WeatherBearApp.appContext.resources.getDrawable(R.drawable.msg_thunder_rainy),
                WeatherBearApp.appContext.resources.getString(R.string.msg_thunder_rainy)),
    }

    enum class DustLevel(var color: Int, var text: String) {
        GOOD(WeatherBearApp.appContext.resources.getColor(R.color.fineDustGood)
                , WeatherBearApp.appContext.resources.getString(R.string.fine_dust_good)),
        NOMAL(WeatherBearApp.appContext.resources.getColor(R.color.fineDustNormal)
                , WeatherBearApp.appContext.resources.getString(R.string.fine_dust_normal)),
        BAD(WeatherBearApp.appContext.resources.getColor(R.color.fineDustBad)
                , WeatherBearApp.appContext.resources.getString(R.string.fine_dust_bad)),
        VERYBAD(WeatherBearApp.appContext.resources.getColor(R.color.fineDustVeryBad)
                , WeatherBearApp.appContext.resources.getString(R.string.fine_dust_very_bad))
    }

    var todayWeatherShape = Weather.SNOW.image
    var todayWeatherText = Weather.SNOW.text
    var todayTemperature = 3
    var todayBodyTemperature = 1
    var todayDust = "100up"
    var todayDustText = DustLevel.BAD.color
    var todayDustColor = DustLevel.BAD.text
    var todayUltraDust = "100up"
    var todayUltraDustText = DustLevel.BAD.color
    var todayUltraDustColor = DustLevel.BAD.text

    var tomorrowWetherText = "오늘보다 선선해요"
    var tomorrowWeatherShape = Weather.SNOW.image
    var tomorrowWeatherText = Weather.SNOW.text
    var tomorrowTemperature = 3
    var tomorrowBodyTemperature = 1
    var tomorrowDust = "100up"
    var tomorrowDustText = DustLevel.BAD.color
    var tomorrowDustColor = DustLevel.BAD.text
    var tomorrowUltraDust = "100up"
    var tomorrowUltraDustText = DustLevel.BAD.color
    var tomorrowUltraDustColor = DustLevel.BAD.text


    private fun updateTodayWeather() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    //데이터가 없을 경우
    private fun checkData() {
        //TODO::대처
    }
}