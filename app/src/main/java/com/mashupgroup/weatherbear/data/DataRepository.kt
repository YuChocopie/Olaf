package com.mashupgroup.weatherbear.data

import android.location.Location
import com.mashupgroup.weatherbear.data.remote.WeatherBearRemoteSource
import com.mashupgroup.weatherbear.models.air.Air
import com.mashupgroup.weatherbear.models.weather.Forecast
import com.mashupgroup.weatherbear.models.weather.Weather
import io.reactivex.Observable

// TODO get data first from local source
object DataRepository {

    private val remoteSource: WeatherBearRemoteSource = WeatherBearRemoteSource()

    fun getAirInfo(location: Location): Observable<Air> = remoteSource.getAirInfo(location)

    fun getWeather(location: Location): Observable<Weather> = remoteSource.getWeather(location)

    fun getForecast(location: Location): Observable<Forecast> = remoteSource.getForecast(location)
}

