package com.mashupgroup.weatherbear

import android.util.Log
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


class AirAPI {

    /** Station Information **/
    fun createStationInfoRetrofit() : Retrofit {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()


        var retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/")
                .build()
        return retrofit!!
    }

    /** Air Information **/
    fun createAirInfoRetrofit() : Retrofit {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

        var retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/")
                .build()
        return retrofit!!
    }
}