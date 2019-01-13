package com.mashupgroup.weatherbear

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil.setContentView
import android.location.Address
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.mashupgroup.weatherbear.Global.createLocationString
import com.mashupgroup.weatherbear.databinding.ActivityMainBinding
import com.mashupgroup.weatherbear.location.ILocationResultListener
import com.mashupgroup.weatherbear.location.LocationHelper
import com.mashupgroup.weatherbear.location.SelectLocationActivity
import com.mashupgroup.weatherbear.models.weather.Forecast
import com.mashupgroup.weatherbear.models.weather.Weather
import com.mashupgroup.weatherbear.viewmodels.BackgroundViewModel
import com.mashupgroup.weatherbear.viewmodels.BearViewModel
import com.mashupgroup.weatherbear.viewmodels.IsDayViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.top_bear.*
import kotlinx.android.synthetic.main.top_toolbar.*
import java.util.*
import android.R.attr.data
import android.view.View
import android.annotation.SuppressLint
import com.mashupgroup.weatherbear.Global.addressList
import com.mashupgroup.weatherbear.models.weather.Forecast
import com.mashupgroup.weatherbear.models.weather.Weather
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    var mainPagerAdapter: MainPagerAdapter = MainPagerAdapter(this)
    private val model = IsDayViewModel()
    private val weatherApiToken = BuildConfig.WEATHER_API_TOKEN
    private val airApiToken = BuildConfig.AIR_API_TOKEN
    private val kakaoApiToken = BuildConfig.KAKAO_API_TOKEN
    private var lat = 0.0
    private var lon = 0.0
    lateinit var mainViewDataBinding: ActivityMainBinding

    private val noAddressBearVM = BearViewModel()
    private val noAddressBgVM = BackgroundViewModel()

    /* API */
    private val airAPI = AirAPI()
    private val airStationRetrofit = airAPI.createStationInfoRetrofit()
    private val airStationInterface = airStationRetrofit.create(AirInterface::class.java)
    private val airRetrofit = airAPI.createAirInfoRetrofit()
    private val airInterface = airRetrofit.create(AirInterface::class.java)

    private val weatherAPI = WeatherAPI()
    private val weatherRetrofit = weatherAPI.createWeatherRetrofit()
    private val weatherInterface = weatherRetrofit.create(WeatherInterface::class.java)


    val kakaoAPI = KakaoAPI()
    val kakaoRetrofit = kakaoAPI.createTransRetrofit()
    val kakaoInterface = kakaoRetrofit.create(KakaoInterface::class.java)

    private val RESULT_CODE_ADDRESS_MANAGE_ACTIVITY = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val permissionModule = PermissionModule()
        permissionModule.setupPermissions(this, this)

        mainViewDataBinding = setContentView(this, R.layout.activity_main)

        //requestTodayWeather()

        // 유저가 저장했었던 주소를 Global.addressList에 불러오기
        Global.loadAddressList()
        // 첫번째 페이지를 현재 위치로 표시할건지 유저가 저장했었던 값 Global.isFirstPageCurrentLocation에 불러오기
        Global.loadIsFirstPageCurrentLocation()

        // 툴바 초기 세팅
        setToolbar()

        // '주소없음' 뷰모델 설정
        noAddressBearVM.weather = BearViewModel.Weather.SUNNY.text
        noAddressBearVM.fineDustData = 1
        noAddressBearVM.setBear()
        noAddressBearVM.updeteBear()
        noAddressBgVM.weather = BackgroundViewModel.Weather.SUNNY.text
        noAddressBgVM.setBackground()

        // ViewPager 초기화
        viewPager.initialize(mainIndicator)
        viewPager.addOnPageChangeListener(object : SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                // ViewPager 페이지가 바뀔 때마다 불림. 데이터 갱신
                setTopViewModelData(position)
            }
        })
        viewPager.adapter = mainPagerAdapter

        // 불러올 위치가 없으면 메시지를 띄움. 불러올 위치가 있으면 데이터 세팅
        if (!Global.isFirstPageCurrentLocation && Global.addressList.size == 0) {
            showNoAddress()
        } else {
            initWithSavedAddressData()
        }

        // 불러올 위치가 없을 때 띄워줄 메시지에 터치리스너 추가
        tvMainEmpty.setOnClickListener {
            val intent = Intent(this, SelectLocationActivity::class.java)
            startActivityForResult(intent, RESULT_CODE_ADDRESS_MANAGE_ACTIVITY)
        }

        var loactionListener = object : ILocationResultListener {
            override fun onLocationReady(location: Location?, address: Address?) {

                if(location == null) {
                    Toast.makeText(this@MainActivity, R.string.err_str_location_ready, Toast.LENGTH_SHORT).show()
                } else {

                    lat = location.latitude
                    lon = location.longitude

                    // '첫번째 아이템이 현위치' 옵션이 켜져있다면 첫번째 아이템 갱신
                    if(Global.isFirstPageCurrentLocation && mainPagerAdapter.count > 0) {
                        requestItemUpdate(mainPagerAdapter.itemList[0], location)
                    }
                }
            }
        }
//        val location_test = Location("")
//        location_test.latitude = 37.544016
//        location_test.longitude = 126.995924
//        requestItemUpdate(mainPagerAdapter.itemList[0], location_test)

        LocationHelper.addLocationResultListener(listener = loactionListener)
        LocationHelper.requestLocation(this, true)


    }

    private fun requestWeatherResponse(item: MainPagerItem, weatherInfo: Weather) {
        Log.v("csh Weather", weatherInfo.toString())
        Log.e("loglogweather", "123" + weatherInfo.weather[0].id)
        var weather: String = "SUNNY"
        val weatherId = weatherInfo.weather[0].id
        when (weatherId / 100) {
            2 -> weather = "THUNDER_RAINY"
            3 -> weather = "RAINY"
            6 -> {
                weather = "SNOW"
                if (weatherId == 621 || weatherId == 622) {
                    weather = "HEAVY_SNOW"
                }
            }
            7 -> {
                weather = "CLOUD"
                if (weatherId == 731 || weatherId == 781) {
                    weather = "WIND"
                }
            }
            8 -> {
                weather = "CLOUD"
                if (weatherId == 800) {
                    weather = "SUNNY"
                }
            }
        }

        //곰의 모습 data
        item.vmBear.weatherData = weather
        item.vmBear.setBear()
        //곰 배경데이터
        item.vmBG.weatherData = weather
        item.vmBG.setBackground()
        //날씨 boxData
        item.vmInfo.todayWeatherData = weather
        item.vmInfo.tomorrowWeatherData = weather
        item.vmInfo.setDayView()

        /* 여기가 오늘의 날씨  */
        /* weatherInfo */
    }

    private fun requestForecastResponse(item: MainPagerItem, forecastInfo: Forecast) {
        Log.v("csh Forecast", forecastInfo.toString())

//        날씨 boxData.. 내일의 날씨
        item.vmInfo.tomorrowWeatherData = "RAIN"
        item.vmInfo.setDayView()

        /* 여기가 5일치 날씨 */
        /* forecastInfo */

        for (i in forecastInfo.list) {
            val dv = java.lang.Long.valueOf(i.dt) * 1000// its need to be in milisecond
            val df = java.util.Date(dv)
            val vv = SimpleDateFormat("MM dd, yyyy hh:mm a").format(df)

        }
    }

    @SuppressLint("CheckResult")
    private fun requestItemUpdate(item: MainPagerItem, location: Location) {

        /* Get TM Postion */
        kakaoInterface.getPos(kakaoApiToken, location.longitude, location.latitude, "WGS84", "TM")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ coord ->
                    var tmX = coord.documents[0].x
                    var tmY = coord.documents[0].y

                    requestStationInfo(item, tmX, tmY)
                }, { error ->
                    error.printStackTrace()
                })

        /* Get Weather */
        weatherInterface.getWeather(location.latitude, location.longitude, weatherApiToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ weatherInfo ->
                    if (weatherInfo != null) {
                        requestWeatherResponse(item, weatherInfo)

                    } else {


                    }

                }, {

                })


        /* Get Forecast */
        weatherInterface.getForecast(location.latitude, location.longitude, weatherApiToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ forecastInfo ->
                    if (forecastInfo != null) {
                        requestForecastResponse(item, forecastInfo)

                    } else {

                    }

                }, {

                })

    }

    private fun requestWeatherInfo(item: MainPagerItem, tmX: String, tmY: String) {
        /* Get WeatherInfo */
        weatherInterface
    }

    @SuppressLint("CheckResult")
    private fun requestStationInfo(item: MainPagerItem, tmX: String, tmY: String) {
        /* Get StationInfo */
        airStationInterface.getStation("json", tmX, tmY, airApiToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ stationInfo ->
                    if (stationInfo != null) {
                        // Air 정보 가져오기
                        requestAirInfo(item, stationInfo.list[0].stationName)
                    } else {

                    }
                }, { error ->
                    error.printStackTrace()
                })
    }

    @SuppressLint("CheckResult")
    private fun requestAirInfo(item: MainPagerItem, stationName: String) {
        /* Get AirInfo */
        airInterface.getAir(getString(R.string.api_json), 1, stationName, getString(R.string.api_daily), 1.3, airApiToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ airInfo ->
                    Log.v("requestAirInfo", "${stationName} : ${airInfo.toString()}")
                    val airItem = airInfo.list[0]

                    //곰의 모습 data
                    item.vmBear.fineDustData = airItem.pm10Grade1h.toInt()
                    item.vmBear.setBear()

                    //날씨 boxData
                    item.vmInfo.todayDustLevelData = airItem.pm10Grade1h.toInt()
                    item.vmInfo.todayDustData = airItem.pm10Value + "up"
                    item.vmInfo.todayUltraDustData = airItem.pm25Value + "up"
                    item.vmInfo.tomorrowDustLevelData = airItem.pm25Grade.toInt()
                    item.vmInfo.tomorrowDustData = airItem.pm10Value24 + "up"
                    item.vmInfo.tomorrowUltraDustData = airItem.pm25Value24 + "up"
                    item.vmInfo.setDayView()

                    mainPagerAdapter.notifyDataSetChanged()
                }, { error ->
                    error.printStackTrace()
                })
    }

    /** 사용자가 저장한 주소 목록이 없을 때 메시지를 띄운다 */
    private fun showNoAddress() {
        mainCoordinatorLayout.post {
            mainScrollView.visibility = View.GONE
            tvMainEmpty.visibility = View.VISIBLE
        }

        // Fake 곰 viewModel 설정
        BearAnimator.stopAnimation(topBearBgWrapper)
        mainViewDataBinding.setVariable(BR.bear, noAddressBearVM)
        mainViewDataBinding.setVariable(BR.bg, noAddressBgVM)
        BearAnimator.startAnimation(topBearBgWrapper)
    }

    /** 사용자가 저장해놨던 주소 목록을 가지고 Adapter(viewPager) 등 초기 세팅을 한다 **/
    private fun initWithSavedAddressData() {
        mainCoordinatorLayout.post {
            mainScrollView.visibility = View.VISIBLE
            tvMainEmpty.visibility = View.GONE
        }

        // 초기화
        mainPagerAdapter.itemList.clear()

        // 첫번째 아이템이 현재 위치면 그거 추가합니다
        if (Global.isFirstPageCurrentLocation) {
            val item = MainPagerItem(BearViewModel(), BackgroundViewModel(), IsDayViewModel(), Address(Locale.getDefault()))
            mainPagerAdapter.addData(item)
        }

        for (addr in Global.addressList) {
            val item = MainPagerItem(BearViewModel(), BackgroundViewModel(), IsDayViewModel(), addr)
            // Todo : 여기에 addr에 맞는 각 ViewModel 세팅을 해야합니다. 아마 날씨 데이터를 불러와야할겁니다.

            // SNOW, RAINY, THUNDER_RAINY, WIND, CLOUD, SUNNY, HEAVY_SNOW
//            곰의 모습 data
            item.vmBear.fineDustData = 3
            item.vmBear.weatherData = "SNOW"
            item.vmBear.setBear()

            //곰 배경데이터
            item.vmBG.weatherData = "HEAVY_SNOW"
            item.vmBG.setBackground()

            //날씨 boxData
            item.vmInfo.tomorrowUltraDustData
            item.vmInfo.todayWeatherData = "CLOUD"
            item.vmInfo.todayTemperatureData = "5"
            item.vmInfo.todayBodyTemperatureData = "1"
//            item.vmInfo.todayDustLevelData = 2
//            item.vmInfo.todayDustData = "100up"
//            item.vmInfo.todayUltraDustData = "100up"

            item.vmInfo.tomorrowWetherTextData = "오늘보다 선선해요"
            item.vmInfo.tomorrowWeatherData = "RAIN"
            item.vmInfo.tomorrowTemperatureData = "3"
            item.vmInfo.tomorrowBodyTemperatureData = "1"
//            item.vmInfo.tomorrowDustLevelData = 2
//            item.vmInfo.tomorrowDustData = "100up"
//            item.vmInfo.tomorrowUltraDustData = "100up"
            item.vmInfo.setDayView()

            mainPagerAdapter.addData(item)
        }

        viewPager.initIndicator()

        // ViewModel업데이트 (처음 아이템으로)
        setTopViewModelData(viewPager.currentItem)
    }

    /**
     *  화면 상단 곰, 바탕화면, 메시지 등을 갱신해주는 메서드
     *  ViewPager.adapter 안에있는 아이템의 뷰모델을 사용하여 갱신
     *  @param position MainPageAdapter에서 가져올 아이템 position
     */
    private fun setTopViewModelData(position: Int) {
        if (position < 0 || position >= mainPagerAdapter.count) {
            return }
        BearAnimator.stopAnimation(topBearBgWrapper)

        val data = mainPagerAdapter.itemList[position]
        mainViewDataBinding.setVariable(BR.bear, data.vmBear)
        mainViewDataBinding.setVariable(BR.bg, data.vmBG)

        val item = MainPagerItem(BearViewModel(), BackgroundViewModel(), IsDayViewModel(), Address(Locale.getDefault()))
        item.vmBG.setBackground()
        item.vmBear.setBear()

        tvSelectedLocation.text = createLocationString(data.address, false)

        BearAnimator.startAnimation(topBearBgWrapper)

        // Todo : 현재 메시지 (오늘은 미세먼지가 심해요! 등) 갱신하는 코드도 있어야함
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.title = ""
        }
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
                startActivityForResult(intent, RESULT_CODE_ADDRESS_MANAGE_ACTIVITY)
            }
            R.id.mnu_info -> {
                // 앱 정보 메뉴 클릭됨
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RESULT_CODE_ADDRESS_MANAGE_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                data?.let { intent ->

                    if(intent.getBooleanExtra("isItemChanged", false)) {
                        // 불러올 위치가 없으면 메시지를 띄움. 불러올 위치가 있으면 데이터 세팅
                        if (!Global.isFirstPageCurrentLocation && Global.addressList.size == 0) {
                            showNoAddress()
                        } else {
                            initWithSavedAddressData()
                        }                    }
                }
            }
        }
    }

    //ACCESS_COARSE_LOCATION,ACCESS_FINE_LOCATION
    fun checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ((ActivityCompat.checkSelfPermission(this@MainActivity
                            , Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED)
                    || (ActivityCompat.checkSelfPermission(this@MainActivity
                            , Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED)) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this
                                , Manifest.permission.ACCESS_COARSE_LOCATION)
                        || ActivityCompat.shouldShowRequestPermissionRationale(this
                                , Manifest.permission.ACCESS_FINE_LOCATION)) {
                    val mToast = Toast.makeText(this, "위치정보 접근 권한이 없음"
                            , Toast.LENGTH_SHORT)
                    mToast.show()
                    //  권한을 한번 취소한적이 있다.
                } else {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission
                            .ACCESS_FINE_LOCATION, Manifest.permission
                            .ACCESS_COARSE_LOCATION), 1000)
                    val mToast = Toast.makeText(this, "위치정보 접근 권한을 허용"
                            , Toast.LENGTH_SHORT)
                    mToast.show()
                }
            } else {
                //권한이 있음
            }
        }
    }
}

