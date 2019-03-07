package com.mashupgroup.weatherbear.api

import com.mashupgroup.weatherbear.models.air.Air
import com.mashupgroup.weatherbear.models.air.Station
import com.mashupgroup.weatherbear.models.weather.Weather
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface AirInterface {

    /** Get Station with TM Position **/
    @GET("getNearbyMsrstnList")
    fun getStation(
            @Query("_returnType") _returnType: String,
            @Query("tmX") tmX: String,
            @Query("tmY") tmY: String,
            @Query("ServiceKey", encoded = true) ServiceKey: String): Observable<Station>

    @GET("getMsrstnAcctoRltmMesureDnsty")
    fun getAir(
            @Query("_returnType") _returnType: String,
            @Query("numOfRows") numOfRows: Int,
            @Query("stationName") stationName: String,
            @Query("dataTerm") dataTerm: String,
            @Query("ver") ver: Double,
            @Query("ServiceKey", encoded = true) ServiceKey: String): Observable<Air>
}