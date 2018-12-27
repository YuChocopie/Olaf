package com.mashupgroup.weatherbear

import android.util.Log
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class KakaoAPI {

    fun createTransRetrofit() : Retrofit {

        var retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://dapi.kakao.com/v2/local/geo/")
                .build()

        return retrofit!!
    }
}