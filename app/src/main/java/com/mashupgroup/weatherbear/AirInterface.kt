package com.mashupgroup.weatherbear

import com.mashupgroup.weatherbear.models.air.Air
import com.mashupgroup.weatherbear.models.weather.Weather
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface AirInterface {
    @GET("getCtprvnMesureSidoLIst")
    fun getAir(
            @Query("_returnType") _returnType: String,
            @Query("sidoName") sidoName: String,
            @Query("numOfRows") numOfRows: String,
            @Query("searchCondition") searchCondition: String,
            @Query("ServiceKey", encoded = true) ServiceKey: String): Observable<Air> //Response<ResponseBody>
}