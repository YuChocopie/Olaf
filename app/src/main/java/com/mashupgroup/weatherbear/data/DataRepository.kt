package com.mashupgroup.weatherbear.data

import android.location.Location
import com.mashupgroup.weatherbear.data.remote.WeatherBearRemoteSource
import com.mashupgroup.weatherbear.models.air.AirItem
import com.mashupgroup.weatherbear.models.air.AirResponse
import com.mashupgroup.weatherbear.models.weather.Forecast
import com.mashupgroup.weatherbear.models.weather.Weather
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

// TODO get data first from local source
object DataRepository {

    private val remoteSource: WeatherBearRemoteSource = WeatherBearRemoteSource()

    fun getAirInfo(location: Location): Observable<AirResponse> = remoteSource.getAirInfo(location)

    fun getWeather(location: Location): Observable<Weather> = remoteSource.getWeather(location)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getForecast(location: Location): Observable<Forecast> = remoteSource.getForecast(location)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

