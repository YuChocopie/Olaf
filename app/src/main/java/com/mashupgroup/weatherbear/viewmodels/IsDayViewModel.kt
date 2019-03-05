package com.mashupgroup.weatherbear.viewmodels

import android.graphics.drawable.Drawable
import com.mashupgroup.weatherbear.R
import com.mashupgroup.weatherbear.WeatherBearApp


class IsDayViewModel {

    //SNOW, RAINY, THUNDER_RAINY, WIND, CLOUD, SUNNY
    //데이터가 안받아지는 경우 아래와 같이..
    var visibleTodayTimeWeatherData = false
    var todayWeatherData = "SUNNY"
    var todayDustLevelData = 0
    var todayUltraDustLevelData = 0
    var todayTemperatureData = "?"
    var todayBodyTemperatureData = WeatherBearApp.appContext.resources
            .getText(R.string.noData) as String
    var todayDustData = ""
    var todayUltraDustData = ""

    var currentTime: Int = 12
    var todayTime: Boolean = true

    var tomorrowWetherBoxTextData = "내일의 정보가 없어요"
    var tomorrowWeatherData = "null"
    var tomorrowTemperatureData = "?"
    var tomorrowBodyTemperatureData = WeatherBearApp.appContext.resources
            .getText(R.string.noData) as String
    var tomorrowDustLevelData = 0
    var tomorrowUltraDustLevelData = 0
    var tomorrowDustData = ""
    var tomorrowUltraDustData = ""

    var todayAfterWeatherDatas = MutableList(8) { "SNOW" }
    var visibleTodayTimeWeather = false

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


    var todayWeatherShape = Weather.SUNNY.image
    var todayWeatherShapeTint = WeatherBearApp.appContext.resources.getColor(R.color.Snow)
    var todayWeatherText = Weather.SUNNY.text
    var todayTemperature = " "
    var todayBodyTemperature = WeatherBearApp.appContext.resources.getText(R.string.noData) as String
    var todayDustText = WeatherBearApp.appContext.resources.getText(R.string.noData) as String
    var todayDustColor = DustLevel.GOOD.color
    var todayDust = " "
    var todayUltraDust = " "
    var todayUltraDustText = WeatherBearApp.appContext.resources.getText(R.string.noData) as String
    var todayUltraDustColor = DustLevel.GOOD.color

    var tomorrowWeatherShape = Weather.SUNNY.image
    var tomorrowWeatherShapeTint = WeatherBearApp.appContext.resources.getColor(R.color.Snow)
    var tomorrowWeatherBoxText = "오늘은 좀 선선해요"
    var tomorrowWeatherText = Weather.SUNNY.text
    var tomorrowTemperature = " "
    var tomorrowBodyTemperature = WeatherBearApp.appContext.resources
            .getText(R.string.noData) as String
    var tomorrowDust = " "
    var tomorrowDustText = WeatherBearApp.appContext.resources.getText(R.string.noData) as String
    var tomorrowDustColor = DustLevel.GOOD.color
    var tomorrowUltraDust = " "
    var tomorrowUltraDustText = WeatherBearApp.appContext.resources.getText(R.string.noData) as String
    var tomorrowUltraDustColor = DustLevel.GOOD.color

    var ivTodayTimeWeathers = MutableList(8) { Weather.SUNNY.image }

    var graphTime: Int = 0
    var tvTodayTimes = MutableList(8) { todayTimeGraph() }.apply {
        this[0] = currentTime.toString()
    }

    fun todayTimeGraph(): String {
        graphTime = ((graphTime / 3 + 1) * 3) % 24
        if (graphTime < 10)
            return "0" + (graphTime)
        return graphTime.toString()
    }


    fun setDayView() {
        getTime()
        todayTemperature = todayTemperatureData
        todayBodyTemperature = todayBodyTemperatureData
        todayDust = todayDustData
        todayUltraDust = todayUltraDustData

        tomorrowWeatherBoxText = tomorrowWetherBoxTextData
        tomorrowTemperature = tomorrowTemperatureData
        tomorrowBodyTemperature = tomorrowBodyTemperatureData
        tomorrowDust = tomorrowDustData
        tomorrowUltraDust = tomorrowUltraDustData

        if (visibleTodayTimeWeatherData) {
            visibleTodayTimeWeather = visibleTodayTimeWeatherData
            setTodayTimeWeather()
        }
//        visibleTodayTimeWeather = visibleTodayTimeWeatherData
        setTodayWeather()
        setTomorrowWeather()
    }

    private fun getTime() {
        todayTime = currentTime in 6..17
    }

    private fun setTodayTimeWeather() {
        graphTime = currentTime
        for (i in 0..7) {
            if (i == 0) {
                tvTodayTimes[i] = if (currentTime < 10) "0" + currentTime else currentTime.toString()
            } else {
                tvTodayTimes[i] = todayTimeGraph()
            }

            ivTodayTimeWeathers[i] = todayWetherGraph(todayAfterWeatherDatas[i], i)
        }
    }

    private fun todayWetherGraph(todayAfterWeatherData: String, num: Int): Drawable {
        var checkNightTime = ((currentTime / 3 + num) * 3) % 24
        if (checkNightTime < 6 || checkNightTime > 17) {
            when (todayAfterWeatherData) {
                Weather.CLOUD.weatherText -> {
                    return Weather.CLOUD_NIGHT.image
                }
                Weather.SUNNY.weatherText -> {
                    return Weather.SUNNY_NIGHT.image
                }
            }
        }
        when (todayAfterWeatherData) {
            Weather.SNOW.weatherText -> {
                return Weather.SNOW.image
            }
            Weather.HEAVY_SNOW.weatherText -> {
                return Weather.HEAVY_SNOW.image
            }
            Weather.RAINY.weatherText -> {
                return Weather.RAINY.image
            }
            Weather.THUNDER_RAINY.weatherText -> {
                return Weather.THUNDER_RAINY.image
            }
            Weather.WIND.weatherText -> {
                return Weather.WIND.image
            }
            Weather.CLOUD.weatherText -> {
                return Weather.CLOUD.image
            }
            Weather.SUNNY.weatherText -> {
                return Weather.SUNNY.image
            }
        }
        return Weather.CLOUD.image
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
            0 -> {
                todayDustText = WeatherBearApp.appContext.resources.getText(R.string.noData)
                        as String
            }
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
            0 -> {
                todayUltraDustText = WeatherBearApp.appContext.resources.getText(R.string.noData)
                        as String
            }
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
            0 -> {
                tomorrowDustText = WeatherBearApp.appContext.resources.getText(R.string.noData)
                        as String
            }
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
            0 -> {
                tomorrowUltraDustText = WeatherBearApp.appContext.resources.getText(R.string.noData)
                        as String
            }
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