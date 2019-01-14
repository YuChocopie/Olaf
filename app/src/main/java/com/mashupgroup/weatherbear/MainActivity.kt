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
import android.view.View
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
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    var mainPagerAdapter: MainPagerAdapter = MainPagerAdapter(this)
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

    var dayTimeTemperture = intArrayOf(0, 0, 0, 0, 0, 0, 0)

    private val RESULT_CODE_ADDRESS_MANAGE_ACTIVITY = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val permissionModule = PermissionModule()
        permissionModule.setupPermissions(this, this)
        checkLocationPermission()

        mainViewDataBinding = setContentView(this, R.layout.activity_main)

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


        var loactionListener = object : ILocationResultListener {
            override fun onLocationReady(location: Location?, address: Address?) {

                if (location == null) {
                    Toast.makeText(this@MainActivity, R.string.err_str_location_ready, Toast.LENGTH_SHORT).show()
                } else {

                    lat = location.latitude
                    lon = location.longitude

                    // '첫번째 아이템이 현위치' 옵션이 켜져있다면 첫번째 아이템 갱신
                    if (Global.isFirstPageCurrentLocation && mainPagerAdapter.count > 0) {
                        requestItemUpdate(mainPagerAdapter.itemList[0], location)
                    }
                }
            }
        }

        LocationHelper.addLocationResultListener(listener = loactionListener)
        LocationHelper.requestLocation(this, true)


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
    }

    private fun requestWeatherResponse(item: MainPagerItem, weatherInfo: Weather) {
        Log.v("csh Weather", weatherInfo.toString())

        var weather = "SUNNY"
        var temp: Double = weatherInfo.main.temp - 273.15
        weather = weatherCalculation(weatherInfo.weather[0].id)
        //곰의 모습 data
        item.vmBear.weatherData = weather
        item.vmBear.setBear()
        //곰 배경데이터
        item.vmBG.weatherData = weather
        item.vmBG.setBackground()
        //날씨 boxData
        item.vmInfo.todayWeatherData = weather
        item.vmInfo.todayBodyTemperatureData = bodyTemperatureCalculation(temp, weatherInfo.wind
                .speed).toString()
        item.vmInfo.todayTemperatureData = (temp.toInt()).toString()
        item.vmInfo.todayAfterWeatherData00 = weather
        item.vmInfo.setDayView()

        item.graphArray.set(0, temp.toInt())

        item.vmInfo.setDayView()
        /* 여기가 오늘의 날씨  */
        /* weatherInfo */
    }


    private fun requestForecastResponse(item: MainPagerItem, forecastInfo: Forecast) {
        /* 여기가 5일치 날씨 */
        /* forecastInfo */
        //해당장소의시간
        val hTime = Date().hours
        //내일 정오시간의 데이터를 받아옵니다
        var noonTime = (36 - hTime) / 3
        val count = true
        if (hTime % 3 > 0 && count) {
            noonTime++
            !count
        }

        var weather: String = "SUNNY"
        val temp: Double = forecastInfo.list[noonTime].main.temp.toDouble() - 273.15
        weather = weatherCalculation(forecastInfo.list[noonTime].weather[0].id)

        //날씨 boxData
        item.vmInfo.currentTime = hTime
        item.vmInfo.tomorrowWeatherData = weather
        item.vmInfo.tomorrowBodyTemperatureData = bodyTemperatureCalculation(temp,
                forecastInfo.list[noonTime].wind.speed.toDouble()).toString()
        item.vmInfo.tomorrowTemperatureData = (temp.toInt()).toString()
        //오늘의 날씨 그래프
        item.vmInfo.todayAfterWeatherData01 = weatherCalculation(forecastInfo.list[0]
                .weather[0].id)
        item.vmInfo.todayAfterWeatherData02 = weatherCalculation(forecastInfo.list[1]
                .weather[0].id)
        item.vmInfo.todayAfterWeatherData03 = weatherCalculation(forecastInfo.list[2]
                .weather[0].id)
        item.vmInfo.todayAfterWeatherData04 = weatherCalculation(forecastInfo.list[3]
                .weather[0].id)
        item.vmInfo.todayAfterWeatherData05 = weatherCalculation(forecastInfo.list[4]
                .weather[0].id)
        item.vmInfo.todayAfterWeatherData06 = weatherCalculation(forecastInfo.list[5]
                .weather[0].id)
        item.vmInfo.todayAfterWeatherData07 = weatherCalculation(forecastInfo.list[6]
                .weather[0].id)
        item.vmInfo.tomorrowWetherBoxTextData = tomorrowWeatherText((forecastInfo.list[0].main
                .temp.toDouble() - 273.15).toInt(), temp.toInt(), weather)
        item.graphArray.set(1, (forecastInfo.list[1].main.temp.toDouble() - 273.15).toInt())
        item.graphArray.set(2, (forecastInfo.list[2].main.temp.toDouble() - 273.15).toInt())
        item.graphArray.set(3, (forecastInfo.list[3].main.temp.toDouble() - 273.15).toInt())
        item.graphArray.set(4, (forecastInfo.list[4].main.temp.toDouble() - 273.15).toInt())
        item.graphArray.set(5, (forecastInfo.list[5].main.temp.toDouble() - 273.15).toInt())
        item.graphArray.set(6, (forecastInfo.list[6].main.temp.toDouble() - 273.15).toInt())


        item.vmInfo.setDayView()
        for (i in forecastInfo.list) {
            val dv = java.lang.Long.valueOf(i.dt) * 1000// its need to be in milisecond
            val df = java.util.Date(dv)
//            val vv = SimpleDateFormat("MM dd, yyyy hh:mm a").format(df)
//             i.main.temp (Default는 켈빈이므로 temp - 273.15 해야 섭씨온도가 나옵니다!)
        }
    }

    private fun tomorrowWeatherText(currendTemp: Int, tomorrowTemp: Int, tomorrowWeather: String): String {
        if (tomorrowWeather == "HEAVY_SNOW") {
            return "내일은 눈이 아주 많이와요~"
        } else if (tomorrowWeather == "SNOW") {
            return "내일은 눈이와요~"
        } else if (tomorrowWeather == "RAINY") {
            return "내일은 바가와요 우산을 챙기세요"
        }
        if (currendTemp < 5 && currendTemp > tomorrowTemp) {
            return "내일은 오늘보다 추워요"
        } else if (currendTemp < 15 && currendTemp < tomorrowTemp) {
            return "내일은 오늘보다 따듯해요"
        } else if (currendTemp < 15 && currendTemp > tomorrowTemp) {
            return "내일은 오늘보다 쌀쌀해요"
        } else if (currendTemp < 25 && currendTemp > tomorrowTemp) {
            return "내일은 오늘보다 선선해요"
        } else if (currendTemp > 25 && currendTemp > tomorrowTemp) {
            return "내일은 오늘보다 시원해요"
        } else if (currendTemp > 2 && currendTemp < tomorrowTemp) {
            return "내일은 오늘보다 시원해요"
        }
        if (currendTemp > tomorrowTemp) {
            return "내일은 날씨가 " + (currendTemp - tomorrowTemp) + "도 낮아요"
        } else
            return "내일은 날씨가 " + (tomorrowTemp - currendTemp) + "도 "
    }


    private fun bodyTemperatureCalculation(temp: Double, speed: Double): Int {
        val v = Math.pow(speed, 0.16)
        return (13.12 + 0.6215 * temp - 11.37 * v + 0.3965 * v * temp).toInt()
    }

    private fun weatherCalculation(weatherId: Int): String {
        when (weatherId / 100) {
            2 -> return "THUNDER_RAINY"
            3 -> return "RAINY"
            6 -> {
                if (weatherId == 621 || weatherId == 622) {
                    return "HEAVY_SNOW"
                } else {
                    return "SNOW"
                }
            }
            7 -> {
                if (weatherId == 731 || weatherId == 781) {
                    return "WIND"
                } else {
                    return "CLOUD"
                }
            }
            8 -> {
                if (weatherId == 800) {
                    return "SUNNY"
                } else {
                    return "CLOUD"
                }
            }
        }
        return "SUNNY"
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
                    item.vmInfo.todayUltraDustLevelData = checkUltraDustLevel(
                            airItem.pm25Value.toInt())
                    item.vmInfo.todayDustData = airItem.pm10Value + "㎍/㎥"
                    item.vmInfo.todayUltraDustData = airItem.pm25Value + "㎍/㎥"

                    item.vmInfo.tomorrowDustLevelData = airItem.pm10Grade.toInt()
                    item.vmInfo.tomorrowUltraDustLevelData = checkUltraDustLevel(
                            airItem.pm25Value24.toInt())
                    item.vmInfo.tomorrowDustData = airItem.pm10Value24 + "㎍/㎥"
                    item.vmInfo.tomorrowUltraDustData = airItem.pm25Value24 + "㎍/㎥"
                    item.vmInfo.setDayView()

                    mainPagerAdapter.notifyDataSetChanged()
                }, { error ->
                    error.printStackTrace()
                })
    }

    private fun checkUltraDustLevel(ultraDust: Int): Int {
        if (ultraDust < 16) {
            return 1
        } else if (ultraDust in 16..50) {
            return 2
        } else if (ultraDust in 51..100) {
            return 3
        } else {
            return 4
        }
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

        // 초기화
        mainPagerAdapter.itemList.clear()
        viewPager.initIndicator()
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
            val item = MainPagerItem(BearViewModel(), BackgroundViewModel(), IsDayViewModel(),
                    Address(Locale.getDefault()), dayTimeTemperture)
            mainPagerAdapter.addData(item)
        }

        for (addr in Global.addressList) {
            val item = MainPagerItem(BearViewModel(), BackgroundViewModel(), IsDayViewModel(),
                    addr, dayTimeTemperture)

            // 각 아이템의 위치정보에 따라 데이터를 요청합니다.
            val location = Location("")
            location.longitude = addr.longitude
            location.latitude = addr.latitude
            requestItemUpdate(item, location)
            //날씨 boxData
            item.vmInfo.tomorrowWetherBoxTextData = "데이터를 넣어야해요"
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
            return
        }
        BearAnimator.stopAnimation(topBearBgWrapper)

        val data = mainPagerAdapter.itemList[position]
        mainViewDataBinding.setVariable(BR.bear, data.vmBear)
        mainViewDataBinding.setVariable(BR.bg, data.vmBG)
        val item = MainPagerItem(BearViewModel(), BackgroundViewModel(), IsDayViewModel(),
                Address(Locale.getDefault()), dayTimeTemperture)
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

                    if (intent.getBooleanExtra("isItemChanged", false)) {
                        // 불러올 위치가 없으면 메시지를 띄움. 불러올 위치가 있으면 데이터 세팅
                        if (!Global.isFirstPageCurrentLocation && Global.addressList.size == 0) {
                            showNoAddress()
                        } else {
                            initWithSavedAddressData()
                        }
                    }
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
