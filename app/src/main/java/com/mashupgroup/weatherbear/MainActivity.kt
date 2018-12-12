package com.mashupgroup.weatherbear

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.mashupgroup.weatherbear.models.weather.Weather
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bear.*
import android.databinding.DataBindingUtil.setContentView
import android.databinding.ViewDataBinding
import kotlinx.android.synthetic.main.item_today_time_weather.*

class MainActivity : AppCompatActivity() {
    var mainPagerAdapter: MainPagerAdapter = MainPagerAdapter(this);
    private val model = IsDayViewModel()

    val OpenWeatherMapKey = "926ab2fea6549951c324d1dc64014bdb"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var binding: ViewDataBinding = setContentView(this, R.layout.activity_main)
        binding.setVariable(BR.bear, BearViewModel())
//        binding.setVariable(BR.isDayData, IsDayViewModel())

        requestTodayWeather()

        val bear_ear_r: Animation = AnimationUtils.loadAnimation(this, R.anim.bear_ear_right)
        val bear_ear_l: Animation = AnimationUtils.loadAnimation(this, R.anim.bear_ear_left)
        val leg_frong: Animation = AnimationUtils.loadAnimation(this, R.anim.leg_frong)
        val leg_frong_good: Animation = AnimationUtils.loadAnimation(this, R.anim.leg_frong_good)
        val leg_frong_bad: Animation = AnimationUtils.loadAnimation(this, R.anim.leg_frong_bad)
        val umbrella: Animation = AnimationUtils.loadAnimation(this, R.anim.umbrella)
        val bear_mask: Animation = AnimationUtils.loadAnimation(this, R.anim.bear_mask)
        val scarh_high: Animation = AnimationUtils.loadAnimation(this, R.anim.scarf_high)
        val scarh_low: Animation = AnimationUtils.loadAnimation(this, R.anim.scarf_low)
        val pet: Animation = AnimationUtils.loadAnimation(this, R.anim.pet)
        //양 귀 흔들기
        bear_ear_right.startAnimation(bear_ear_r)
        bear_ear_left.startAnimation(bear_ear_l)
        // 양 팔 흔들기
        bear_leg.startAnimation(leg_frong)
        bear_leg_front_good.startAnimation(leg_frong_good)
        //스카프 흔들리기
        bear_scarf_high.startAnimation(scarh_high)
        bear_scarf_low.startAnimation(scarh_low)
        // 비올 때 우산과 팔 동시움직임
        bear_umbrella.startAnimation(umbrella)
        //미세먼지 농도 높을 때 마스크와 마스크 발 동시.
        bear_leg_front_bad.startAnimation(leg_frong_bad)
        handkerchief.startAnimation(bear_mask)
        //먼지 펫 움직이기
        bear_pet.startAnimation(pet)
        bear_pet_small.startAnimation(pet)
        bear_pet_w.startAnimation(pet)
        // ViewPager 초기화
        viewPager.initialize(mainIndicator)
        viewPager.adapter = mainPagerAdapter

        viewPager.initIndicator()
    }


    fun requestTodayWeather() {
        val weatherAPI: WeatherAPI = WeatherAPI()
        val retrofit: Retrofit = weatherAPI.createTodayWeatherRetrofit()

        val api = retrofit.create(WeatherInterface::class.java)

        //TODO: lat, lon 값 수정 필요
        api.getWeather(35.0, 135.0, OpenWeatherMapKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ weather: Weather? ->
                    if (weather != null) {
                        // 현재 기온
                        tvTodayTime.text = String.format("%.1f", (weather.main
                                .temp - 273))
                    }
                }, { error ->
                    error.printStackTrace()
                })
    }

}

