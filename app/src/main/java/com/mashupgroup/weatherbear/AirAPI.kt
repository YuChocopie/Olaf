package com.mashupgroup.weatherbear

import android.util.Log
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import com.google.gson.Gson



class AirAPI {
    fun createTodayAirRetrofit() : Retrofit {

        var retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/")
                .build()
        return retrofit!!
    }
}