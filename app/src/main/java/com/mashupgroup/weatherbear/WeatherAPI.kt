package com.mashupgroup.weatherbear

import android.util.Log
import com.mashupgroup.weatherbear.R.id.tvTodayWeatherTemperature
import com.mashupgroup.weatherbear.models.weather.Weather
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class WeatherAPI {

    fun createTodayWeatherRetrofit() : Retrofit {
        Log.d("WeatherAPI", "createTodayWeatherRetrofit")

        var retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .build()

        return retrofit!!
    }
}