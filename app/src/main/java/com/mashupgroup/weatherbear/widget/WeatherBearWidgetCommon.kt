package com.mashupgroup.weatherbear.widget

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.location.Address
import android.location.Location
import android.location.LocationManager
import com.mashupgroup.weatherbear.*
import com.mashupgroup.weatherbear.Global
import com.mashupgroup.weatherbear.data.DataRepository

abstract class WeatherBearWidgetCommon : AppWidgetProvider() {

    enum class WdgFineDustLvl { GOOD, NORMAL, BAD, WORST, NO_DATA }
    enum class WdgWeather { SUNNY, SNOW, RAIN, CLOUDY, THUNDER, NO_DATA }

    // 정보 갱신동안 저장할 context, appWidgetManager, appWidgetIds
    // 얘들은 updateAppWidget 최종호출시점때 null로 메모리 해제되어야한다.
    private var savedContext: Context? = null
    private var savedAppWidgetManager: AppWidgetManager? = null
    private var savedAppWidgetIds: IntArray? = null
    /* 사실 위 방법이 좋은건 아닌거같은데, 이 3개를 일일이 매개변수로 넘겨주는 것 보단 덜 귀찮을거같아서 이렇게 했어.
       null-check 를 하지만 null-safe 한거는 아니야. 구조체로 묶는것도 좀 오바인거같고. 더 나은방법 없을까..? */

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // 임시 객체 저장
        savedContext = context
        savedAppWidgetManager = appWidgetManager
        savedAppWidgetIds = appWidgetIds

        // 날씨 정보 갱신
        requestWeatherData()
    }

    abstract fun updateAppWidget(weatherData : WidgetWeatherData,
                                 context: Context,
                                 appWidgetManager: AppWidgetManager,
                                 appWidgetId: Int)

    private fun requestWeatherData() {
        if(savedContext == null || savedAppWidgetManager == null || savedAppWidgetIds == null) return

        val widgetWeatherData = WidgetWeatherData(
                WdgWeather.NO_DATA,
                WdgFineDustLvl.NO_DATA,
                savedContext!!.getString(R.string.noData),
                0)

        // 저장된 장소중 첫번째 가져오기
        if(Global.loadAddressList() != null) {
            val addrList = Global.loadAddressList()
            if(addrList!!.isEmpty()) {
                // 저장된 장소가 없으므로 no data 처리
                widgetWeatherData.isDustLevelUpdated = true
                widgetWeatherData.isWeatherUpdated = true
                updateWeatherData(widgetWeatherData)
            }

            // 저장된 장소가 있으므로 장소(첫번째꺼) 가져옴
            val addrFirst: Address = addrList[0]
            val location = Location(LocationManager.GPS_PROVIDER)
            location.latitude = addrFirst.latitude
            location.longitude = addrFirst.longitude
            widgetWeatherData.locationText = Global.getSmallestLocationString(addrFirst)

            requestDataFromServer(widgetWeatherData, location)

        } else {
            // 저장된 장소가 없으므로 no data 처리
            widgetWeatherData.isDustLevelUpdated = true
            widgetWeatherData.isWeatherUpdated = true
            updateWeatherData(widgetWeatherData)
        }
    }

    private fun updateWeatherData(data: WidgetWeatherData) {
        if(savedContext == null || savedAppWidgetManager == null || savedAppWidgetIds == null) return
        // 날씨 업데이트되면 한번, 미세먼지 업데이트되면 한번 해서 총 두번 호출될것임

        for (appWidgetId in savedAppWidgetIds!!)         // 모든 활성화된 위젯 업데이트
            updateAppWidget(data, savedContext!!, savedAppWidgetManager!!, appWidgetId)

        // 저장된 임시객체 메모리해제(모든 데이터가 다 불러와졌을때)
        if(data.isWeatherUpdated && data.isDustLevelUpdated) {
            savedAppWidgetIds = null
            savedAppWidgetManager = null
            savedContext = null
        }
    }

    @SuppressLint("CheckResult")
    private fun requestDataFromServer(data: WidgetWeatherData, location: Location) {
        if(savedContext == null || savedAppWidgetManager == null || savedAppWidgetIds == null) return

        /* Get TM Postion */
        DataRepository.getAirInfo(location)
                .subscribe({ items ->
                    data.isDustLevelUpdated = true
                    val dustLevel = items[0].pm10Grade.toInt()
                    data.fineDustLevel = when(dustLevel) {
                        1 -> WdgFineDustLvl.GOOD
                        2 -> WdgFineDustLvl.NORMAL
                        3 -> WdgFineDustLvl.BAD
                        4 -> WdgFineDustLvl.WORST
                        else -> WdgFineDustLvl.NO_DATA
                    }
                    updateWeatherData(data)
                }, { error ->
                    error.printStackTrace()
                })

        /* Get WdgWeather */
        DataRepository.getWeather(location)
                .subscribe({ weatherInfo ->
                    data.isWeatherUpdated = true
                    val weatherId = weatherInfo.weather[0].id
                    when (weatherId / 100) {
                        2 -> data.weather = WdgWeather.THUNDER
                        3 -> data.weather = WdgWeather.RAIN
                        6 -> data.weather = WdgWeather.SNOW
                        7 -> data.weather = WdgWeather.CLOUDY
                        8 -> if (weatherId == 800) data.weather = WdgWeather.SUNNY
                        else data.weather = WdgWeather.CLOUDY
                        else -> data.weather = WdgWeather.SUNNY
                    }
                    data.temperature = (weatherInfo.main.temp - 273.15).toInt()
                    updateWeatherData(data)
                }, { error ->
                    error.printStackTrace()
                })
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    data class WidgetWeatherData(var weather: WdgWeather,
                                 var fineDustLevel: WdgFineDustLvl,
                                 var locationText: String,
                                 var temperature: Int) {
        var isWeatherUpdated = false
        var isDustLevelUpdated = false
    }
}