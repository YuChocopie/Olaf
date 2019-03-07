package com.mashupgroup.weatherbear.data.remote

import android.location.Location
import com.mashupgroup.weatherbear.BuildConfig
import com.mashupgroup.weatherbear.models.air.Air
import com.mashupgroup.weatherbear.models.air.Station
import com.mashupgroup.weatherbear.models.kakao.Coord
import com.mashupgroup.weatherbear.models.weather.Forecast
import com.mashupgroup.weatherbear.models.weather.Weather
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

class WeatherBearRemoteSource {

    private val airService: AirService
    private val weatherService: WeatherService
    private val transcoordService: TranscoordService

    init {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

        airService = Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://openapi.airkorea.or.kr/openapi/services/rest/")
                .build()
                .create(AirService::class.java)

        weatherService = Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .build()
                .create(WeatherService::class.java)

        transcoordService = Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://dapi.kakao.com/v2/local/geo/")
                .build()
                .create(TranscoordService::class.java)
    }

    fun getAirInfo(location: Location): Observable<Air> = transcoordService
            .getPos(location.longitude, location.latitude)
            .subscribeOn(Schedulers.io())
            .concatMap { airService.getStation(it.documents[0].x, it.documents[0].y) }
            .concatMap { airService.getAir(it.list[0].stationName) }
            .observeOn(AndroidSchedulers.mainThread())


    fun getWeather(location: Location): Observable<Weather> = weatherService
            .getWeather(location.latitude, location.longitude)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getForecast(location: Location): Observable<Forecast> = weatherService
            .getForecast(location.latitude, location.longitude)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

}

interface AirService {

    /** Get Station with TM Position **/
    @GET("MsrstnInfoInqireSvc/getNearbyMsrstnList?_returnType=json&ServiceKey=${BuildConfig.AIR_API_TOKEN}")
    fun getStation(
            @Query("tmX") tmX: String,
            @Query("tmY") tmY: String): Observable<Station>

    @GET("ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?_returnType=json&ServiceKey=${BuildConfig.AIR_API_TOKEN}&numOfRows=1&dataTerm=DAILY&ver=1.3")
    fun getAir(@Query("stationName") stationName: String): Observable<Air>
}

interface WeatherService {

    @GET("weather?appid=${BuildConfig.WEATHER_API_TOKEN}")
    fun getWeather(
            @Query("lat") lat: Double,
            @Query("lon") lon: Double): Observable<Weather>

    @GET("forecast?appid=${BuildConfig.WEATHER_API_TOKEN}")
    fun getForecast(
            @Query("lat") lat: Double,
            @Query("lon") lon: Double): Observable<Forecast>
}


interface TranscoordService {

    @Headers("Authorization: ${BuildConfig.KAKAO_API_TOKEN}")
    @GET("transcoord.json?input_coord=WGS84&output_coord=TM")
    fun getPos(
            @Query("x") x: Double,
            @Query("y") y: Double): Observable<Coord>//Response<ResponseBody>
}