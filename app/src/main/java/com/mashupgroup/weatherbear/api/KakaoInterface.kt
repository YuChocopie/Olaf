package com.mashupgroup.weatherbear.api


import com.mashupgroup.weatherbear.models.kakao.Coord
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoInterface {

    @GET("transcoord.json")
    fun getPos(
            @Header("Authorization") Authorization: String,
            @Query("x") x: Double,
            @Query("y") y: Double,
            @Query("input_coord") input_coord: String,
            @Query("output_coord") output_coord: String): Observable<Coord>//Response<ResponseBody>
}