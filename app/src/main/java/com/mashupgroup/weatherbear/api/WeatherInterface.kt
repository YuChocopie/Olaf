package com.mashupgroup.weatherbear.api

import com.mashupgroup.weatherbear.models.weather.Forecast
import com.mashupgroup.weatherbear.models.weather.Weather
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherInterface {

    @GET("weather")
    fun getWeather(
            @Query("lat") lat: Double,
            @Query("lon") lon: Double,
            @Query("appid") appid: String): Observable<Weather>

    @GET("forecast")
    fun getForecast(
            @Query("lat") lat: Double,
            @Query("lon") lon: Double,
            @Query("appid") appid: String): Observable<Forecast>
}