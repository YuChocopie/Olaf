package com.mashupgroup.weatherbear

import android.content.Intent
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
import android.location.Address
import android.location.Location
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.mashupgroup.weatherbear.databinding.ActivityMainBinding
import com.mashupgroup.weatherbear.location.ILocationResultListener
import com.mashupgroup.weatherbear.location.LocationHelper
import com.mashupgroup.weatherbear.location.SelectLocationActivity
import kotlinx.android.synthetic.main.item_bear_background.*
import kotlinx.android.synthetic.main.item_today_time_weather.*
import kotlinx.android.synthetic.main.top_toolbar.*
import java.util.*

class MainActivity : AppCompatActivity() {
    var mainPagerAdapter: MainPagerAdapter = MainPagerAdapter(this)
    private val model = IsDayViewModel()
    private val weatherApiToken = BuildConfig.WEATHER_API_TOKEN
    private val airApiToken = BuildConfig.AIR_API_TOKEN
    private var lat = 0.0
    private var lon = 0.0
    lateinit var mainViewDataBinding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val permissionModule = PermissionModule()
        permissionModule.setupPermissions(this, this)

        mainViewDataBinding = setContentView(this, R.layout.activity_main)

        var loactionListener = object : ILocationResultListener {
            override fun onLocationReady(location: Location?, address: Address?) {

                if(location == null) {
                    Toast.makeText(this@MainActivity, R.string.err_str_location_ready, Toast.LENGTH_SHORT).show()
                } else {
                    lat = location.latitude
                    lon = location.longitude
                }
            }
        }

        LocationHelper.addLocationResultListener(listener = loactionListener)
        LocationHelper.requestLocation(this, true)

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

        ///눈내림
        val snow1: Animation = AnimationUtils.loadAnimation(this, R.anim.bg_snow_1)
        val snow1_1: Animation = AnimationUtils.loadAnimation(this, R.anim.bg_snow_1_2)
        val snow2: Animation = AnimationUtils.loadAnimation(this, R.anim.bg_snow_2)
        val snow2_2: Animation = AnimationUtils.loadAnimation(this, R.anim.bg_snow_2_2)
        ivSnow1.startAnimation(snow1)
        ivSnow1_2.startAnimation(snow1_1)
        ivSnow2.startAnimation(snow2)
        ivSnow2_2.startAnimation(snow2_2)

        //비내림
        val rain1: Animation = AnimationUtils.loadAnimation(this, R.anim.bg_rainy_1)
        val rain2: Animation = AnimationUtils.loadAnimation(this, R.anim.bg_rainy_2)
        val rain3: Animation = AnimationUtils.loadAnimation(this, R.anim.bg_rainy_3)
        ivRain1.startAnimation(rain1)
        ivRain2.startAnimation(rain2)
        ivRain2_3.startAnimation(rain3)

        //눈내림
        val cloud1: Animation = AnimationUtils.loadAnimation(this, R.anim.bg_cloud_1)
        val cloud2: Animation = AnimationUtils.loadAnimation(this, R.anim.bg_cloud_2)
        val cloud3: Animation = AnimationUtils.loadAnimation(this, R.anim.bg_cloud_3)
        ivCloud1.startAnimation(cloud1)
        ivCloud1_2.startAnimation(cloud1)
        ivCloud2.startAnimation(cloud2)
        ivCloud2_2.startAnimation(cloud2)
        ivCloud3.startAnimation(cloud3)


        setToolbar()

        // ViewPager 초기화
        viewPager.initialize(mainIndicator)
        viewPager.addOnPageChangeListener(object : SimpleOnPageChangeListener() {
            override fun onPageSelected(position : Int) {
                // ViewPager 페이지가 바뀔 때마다 불림. 데이터 갱신
                setTopViewModelData(position)
            }
        })
        viewPager.adapter = mainPagerAdapter

        var vm1 = MainPagerItem(BearViewModel(), BackgroundViewModel(), IsDayViewModel(), Address(Locale.getDefault()))
        var vm2 = MainPagerItem(BearViewModel(), BackgroundViewModel(), IsDayViewModel(), Address(Locale.getDefault()))

        mainPagerAdapter.addData(vm1)
        mainPagerAdapter.addData(vm2)
        vm2.vmInfo.todayTemperature = "0"
        viewPager.initIndicator()

        // ViewModel업데이트 (처음 아이템으로)
        setTopViewModelData(viewPager.currentItem)
    }

    /**
     *  화면 상단 곰, 바탕화면, 메시지 등을 갱신해주는 메서드
     *  ViewPager.adapter 안에있는 아이템의 뷰모델을 사용하여 갱신
     *  @param position MainPageAdapter에서 가져올 아이템 position
     */
    private fun setTopViewModelData(position : Int) {
        val data = mainPagerAdapter.itemList[position]
        mainViewDataBinding.setVariable(BR.bear, data.vmBear)
        mainViewDataBinding.setVariable(BR.bg, data.vmBG)
        tvSelectedLocation.text = createLocationString(data.address)
        // Todo : 현재 메시지 (오늘은 미세먼지가 심해요! 등) 갱신하는 코드도 있어야함
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.title = ""
        }
    }



    fun requestTodayWeather() {
        val weatherAPI: WeatherAPI = WeatherAPI()
        val retrofit: Retrofit = weatherAPI.createTodayWeatherRetrofit()

        val api = retrofit.create(WeatherInterface::class.java)

        //TODO: lat, lon 값 수정 필요
        api.getWeather(35.0, 135.0, weatherApiToken)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.mnu_select_location -> {
                // 위치 선택 메뉴 클릭됨
                val intent = Intent(this, SelectLocationActivity::class.java)
                startActivity(intent)
            }
            R.id.mnu_info -> {
                // 앱 정보 메뉴 클릭됨
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun createLocationString(address : Address) : String {
        val sb = StringBuilder()
        sb.append(address.countryName)
        sb.append(address.subAdminArea)

        return sb.toString()
    }
}

