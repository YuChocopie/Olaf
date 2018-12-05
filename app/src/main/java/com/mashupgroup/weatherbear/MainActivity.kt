package com.mashupgroup.weatherbear

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.mashupgroup.weatherbear.models.weather.Weather
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.item_today_weather.*
import retrofit2.Retrofit


class MainActivity : AppCompatActivity() {
    val OpenWeatherMapKey = "926ab2fea6549951c324d1dc64014bdb"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestTodayWeather()
    }

    fun requestTodayWeather() {
        val weatherAPI : WeatherAPI = WeatherAPI()
        val retrofit : Retrofit = weatherAPI.createTodayWeatherRetrofit()

        val api = retrofit.create(WeatherInterface::class.java)

        //TODO: lat, lon 값 수정 필요
        api.getWeather(35.0, 135.0, OpenWeatherMapKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ weather : Weather? ->
                    if(weather != null) {
                        // 현재 기온
                        tvTodayWeatherTemperature.text = String.format("%.1f",(weather.main.temp-273))
                    }
                }, {
                    error -> error.printStackTrace()
                })
    }

}

