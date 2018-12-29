package com.mashupgroup.weatherbear

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.mashupgroup.weatherbear.models.weather.Weather
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import kotlinx.android.synthetic.main.activity_main.*
import android.databinding.DataBindingUtil.setContentView
import android.location.Address
import android.location.Location
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.mashupgroup.weatherbear.Global.createLocationString
import com.mashupgroup.weatherbear.databinding.ActivityMainBinding
import com.mashupgroup.weatherbear.location.ILocationResultListener
import com.mashupgroup.weatherbear.location.LocationHelper
import com.mashupgroup.weatherbear.location.SelectLocationActivity
import com.mashupgroup.weatherbear.models.air.Air
import com.mashupgroup.weatherbear.viewmodels.BackgroundViewModel
import com.mashupgroup.weatherbear.viewmodels.BearViewModel
import com.mashupgroup.weatherbear.viewmodels.IsDayViewModel
import kotlinx.android.synthetic.main.item_today_time_weather.*
import kotlinx.android.synthetic.main.top_bear.*
import kotlinx.android.synthetic.main.top_toolbar.*
import java.util.*

class MainActivity : AppCompatActivity() {
    var mainPagerAdapter: MainPagerAdapter = MainPagerAdapter(this)
    private val model = IsDayViewModel()
    private val weatherApiToken = BuildConfig.WEATHER_API_TOKEN
    private val airApiToken = BuildConfig.AIR_API_TOKEN
    private val kakaoApiToken = BuildConfig.KAKAO_API_TOKEN
    private var lat = 0.0
    private var lon = 0.0
    lateinit var mainViewDataBinding : ActivityMainBinding

    /* API */
    private val airAPI = AirAPI()
    private val airStationRetrofit = airAPI.createStationInfoRetrofit()
    private val airStationInterface = airStationRetrofit.create(AirInterface::class.java)
    private val airRetrofit = airAPI.createAirInfoRetrofit()
    private val airInterface = airRetrofit.create(AirInterface::class.java)

    val kakaoAPI = KakaoAPI()
    val kakaoRetrofit = kakaoAPI.createTransRetrofit()
    val kakaoInterface = kakaoRetrofit.create(KakaoInterface::class.java)

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

        // 곰돌이 애니메이션 초기화 및 시작
        BearAnimator.startAnimation(topBearBgWrapper)

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

        vm1.vmBG.setBackground()
        vm1.vmBear.setBear()
        vm1.vmInfo.setDayView()

        mainPagerAdapter.addData(vm1)
        mainPagerAdapter.addData(vm2)
        vm2.vmInfo.todayTemperature = "0"
        viewPager.initIndicator()

        // ViewModel업데이트 (처음 아이템으로)
        setTopViewModelData(viewPager.currentItem)

        var loactionListener = object : ILocationResultListener {
            override fun onLocationReady(location: Location?, address: Address?) {

                if(location == null) {
                    Toast.makeText(this@MainActivity, R.string.err_str_location_ready, Toast.LENGTH_SHORT).show()
                } else {
                    lat = location.latitude
                    lon = location.longitude

                    requestItemUpdate(vm1, location)
                }
            }
        }

        LocationHelper.addLocationResultListener(listener = loactionListener)
        LocationHelper.requestLocation(this, true)


    }

    private fun requestItemUpdate(item: MainPagerItem, location: Location) {

        /* Get TM Postion */
        kakaoInterface.getPos(kakaoApiToken, location.longitude, location.latitude, "WGS84", "TM")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({coord->
                    var tmX = coord.documents[0].x
                    var tmY = coord.documents[0].y
                    requestStationInfo(item, tmX, tmY)
                }, { error->
                    error.printStackTrace()
                })
    }

    private fun requestStationInfo(item: MainPagerItem, tmX: String, tmY: String) {
        /* Get StationInfo */
        airStationInterface.getStation("json",tmX,tmY,airApiToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ stationInfo ->
                    if(stationInfo != null) {
                        // Air 정보 가져오기
                        requestAirInfo(item, stationInfo.list[0].stationName)
                    } else {

                    }
                }, { error ->
                    error.printStackTrace()
                })
    }

    private fun requestAirInfo(item: MainPagerItem, stationName: String) {
        /* Get AirInfo */
        airInterface.getAir(getString(R.string.api_json), 1, stationName, getString(R.string.api_daily), 1.3, airApiToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ airInfo ->
                    Log.v("requestAirInfo", "${stationName} : ${airInfo.toString()}")
                    val airItem = airInfo.list[0]

                    item.vmInfo.todayDust = airItem.pm10Value
                    item.vmInfo.todayUltraDust = airItem.pm25Value

                    item.vmInfo.tomorrowDust = airItem.pm10Value24
                    item.vmInfo.tomorrowUltraDust = airItem.pm25Value24

                    mainPagerAdapter.notifyDataSetChanged()
                }, { error ->
                    error.printStackTrace()
                })
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

