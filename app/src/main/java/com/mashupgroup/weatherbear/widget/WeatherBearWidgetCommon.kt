package com.mashupgroup.weatherbear.widget

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.location.Address
import android.location.Location
import android.location.LocationManager
import android.util.Log
import com.mashupgroup.weatherbear.*
import com.mashupgroup.weatherbear.api.*
import com.mashupgroup.weatherbear.Global
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class WeatherBearWidgetCommon : AppWidgetProvider() {
    /* API */
    private val weatherApiToken = BuildConfig.WEATHER_API_TOKEN
    private val airApiToken = BuildConfig.AIR_API_TOKEN
    private val kakaoApiToken = BuildConfig.KAKAO_API_TOKEN
    private val airAPI = AirAPI()
    private val airStationRetrofit = airAPI.createStationInfoRetrofit()
    private val airStationInterface = airStationRetrofit.create(AirInterface::class.java)
    private val airRetrofit = airAPI.createAirInfoRetrofit()
    private val airInterface = airRetrofit.create(AirInterface::class.java)
    private val weatherAPI = WeatherAPI()
    private val weatherRetrofit = weatherAPI.createWeatherRetrofit()
    private val weatherInterface = weatherRetrofit.create(WeatherInterface::class.java)
    private val kakaoAPI = KakaoAPI()
    private val kakaoRetrofit = kakaoAPI.createTransRetrofit()
    private val kakaoInterface = kakaoRetrofit.create(KakaoInterface::class.java)

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
        kakaoInterface.getPos(kakaoApiToken, location.longitude, location.latitude, "WGS84", "TM")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( { coord -> coord?.let { requestStationInfo(data, coord.documents[0].x, coord.documents[0].y, savedContext!!) } },
                            { err -> err.printStackTrace() })

        /* Get WdgWeather */
        weatherInterface.getWeather(location.latitude, location.longitude, weatherApiToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( { weatherInfo ->
                    weatherInfo?.let {
                        data.isWeatherUpdated = true
                        val weatherId = it.weather[0].id
                        when (weatherId / 100) {
                            2 -> data.weather = WdgWeather.THUNDER
                            3 -> data.weather = WdgWeather.RAIN
                            6 -> data.weather = WdgWeather.SNOW
                            7 -> data.weather = WdgWeather.CLOUDY
                            8 -> if (weatherId == 800) data.weather = WdgWeather.SUNNY
                                 else data.weather = WdgWeather.CLOUDY
                            else -> data.weather = WdgWeather.SUNNY
                        }
                        data.temperature = (it.main.temp - 273.15).toInt()
                        updateWeatherData(data)
                    }
                }, { err -> err.printStackTrace() })
    }

    @SuppressLint("CheckResult")
    private fun requestStationInfo(data: WidgetWeatherData, tmX: String, tmY: String, context: Context) {
        if(savedContext == null || savedAppWidgetManager == null || savedAppWidgetIds == null) return

        /* Get StationInfo */
        airStationInterface.getStation("json", tmX, tmY, airApiToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( { stationInfo -> stationInfo?.let{ station -> requestAirInfo(data, station.list[0].stationName, context) } },
                            { err -> err.printStackTrace() })
    }

    @SuppressLint("CheckResult")
    private fun requestAirInfo(data: WidgetWeatherData, stationName: String, context: Context) {
        if(savedContext == null || savedAppWidgetManager == null || savedAppWidgetIds == null) return

        /* Get AirInfo */
        airInterface.getAir(context.getString(R.string.api_json), 1, stationName, context.getString(R.string.api_daily), 1.3, airApiToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ airInfo ->
                    data.isDustLevelUpdated = true
                    Log.v("requestAirInfo", "$stationName : $airInfo")
                    val dustLevel = airInfo.list[0].pm10Grade.toInt()
                    data.fineDustLevel = when(dustLevel) {
                        1 -> WdgFineDustLvl.GOOD
                        2 -> WdgFineDustLvl.NORMAL
                        3 -> WdgFineDustLvl.BAD
                        4 -> WdgFineDustLvl.WORST
                        else -> WdgFineDustLvl.NO_DATA
                    }
                    updateWeatherData(data)

                }, { err -> err.printStackTrace() })
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