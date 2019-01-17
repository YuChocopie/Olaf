package com.mashupgroup.weatherbear.location

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Toast

import java.io.IOException

import android.content.Context.LOCATION_SERVICE
import com.mashupgroup.weatherbear.R
import com.mashupgroup.weatherbear.WeatherBearApp
import java.util.*
import android.location.Criteria

/** jujin.kim
 * Location Helper
 * 지역 정보를 얻어오는 클래스(싱글턴)
 *
 * 생성 후 requestLocation로 위치 요청 및 콜백메서드를 통해 위치 요청 결과를 받아올 수 있다.
 * 사용을 위해 COARSE & FINE LOCATION 권한이 반드시 필요하며 스플래시 스크린에서 꼭 권한 따길 바람.
 *
 * 위치 요청 후 결과를 받기 위해 꼭 ILocationResultListener 리스너를 등록해야함 (add~~메서드 사용)
 */
object LocationHelper {
    /** 마지막으로 저장된 위치 **/
    lateinit var lastLocation: Location; private set

    /** 현재 위치 요청중인지? (중복요청방지) */
    private var isLocationRequestedCurrently: Boolean = false

    /** 이 클래스에서 사용할 LocationManager(시스템서비스) 인스턴스 */
    private var mLocationManager: LocationManager = WeatherBearApp.appContext.getSystemService(LOCATION_SERVICE) as LocationManager

    /** 위치 정보를 받을 리스너 리스트 */
    private var mLocationResultListenerList = ArrayList<ILocationResultListener>()

    /**
     * 마지막으로 저장된 위치 정보의 Address 개체를 가져온다. (도시나 지역 정보)
     * GeoCoder를 이용한다
     * @return 위치 주소 Address개체 (없을경우 null)
     */
    fun  getLastSavedAddress(): Address? {
        val context = WeatherBearApp.appContext
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            return geocoder.getFromLocation(
                    lastLocation.latitude,
                    lastLocation.longitude, 1
            )[0]
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        } catch (exception : Exception) {
            exception.printStackTrace()
        }

        return null
    }

    /**
     * 위치를 1회 요청한다. 위치를 성공적으로 가져올 경우 모든 리스너로 위치 정보를 뿌린다.
     * (콜백부를 때 매개변수로 Location(위도, 경도)와 Address(주소 정보)를 넘겨준다)
     * Address.getCountryName으로 나라 이름, getLocality로 지역 이름 등을 가져올 수 있다.
     * @param activityContext
     *      Activity context
     * @param includeLatestLocation
     *      true이면 최근에 얻었던 위치를 먼저 반환 후 갱신된 위치를 다시 반환, false면 갱신된 위치만 반환
     * @return 요청 실패시(권한X, 또는 이미 요청중) false, 성공시 true 반환
     */
    fun requestLocation(activityContext: Activity, includeLatestLocation: Boolean): Boolean {
        if (isLocationRequestedCurrently) {
            Log.v(this.javaClass.name, "Requesting location is already in progress.")
            return false
        }

        mLocationManager = activityContext.getSystemService(LOCATION_SERVICE) as LocationManager

        // 위치 권한 체크
        if (ActivityCompat.checkSelfPermission(
                        activityContext,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        activityContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            // If no permission, abort request.
            /** The location permission must be granted at the splash screen  */
            val mToast = Toast.makeText(activityContext, R.string.unable_get_locaiton, Toast.LENGTH_SHORT)
            mToast.show()

            return false
        }

        // 마지막에 받아온 GPS정보가 남아있다면 일단 걔부터 뿌림
        val lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        lastKnownLocation.let { location ->
            lastLocation = location

            // 등록된 모든 리스너에게 위치정보를 뿌림
            if(includeLatestLocation) {
                for (listener in mLocationResultListenerList) {
                    listener.onLocationReady(lastLocation, getLastSavedAddress())
                }
            }
        }

        // 위치정보를 새로 요청하기 위한 조건 생성
        val criteria = Criteria()
        criteria.accuracy = Criteria.ACCURACY_COARSE
        criteria.powerRequirement = Criteria.POWER_LOW
        criteria.isAltitudeRequired = false
        criteria.isBearingRequired = false
        criteria.isSpeedRequired = false
        criteria.isCostAllowed = true
        criteria.horizontalAccuracy = Criteria.ACCURACY_HIGH
        criteria.verticalAccuracy = Criteria.ACCURACY_HIGH

        // 새로운 위치정보 1회 요청
        mLocationManager.requestSingleUpdate(criteria, mLocationListener, null)

        return true
    }

    /** LocationManager에 위치 요청할 때 사용할 내부적인 리스너 */
    private val mLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            // 마지막 location 정보를 저장
            lastLocation = location

            // 등록된 모든 리스너에게 위치정보를 뿌림
            for(listener in mLocationResultListenerList) {
                listener.onLocationReady(lastLocation, getLastSavedAddress())
            }
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    /**
     * 위치 리스너를 등록한다.
     * 위치 요청시 등록된 모든 리스너로 결과를 알린다.
     */
    fun addLocationResultListener(listener : ILocationResultListener) {
        if(mLocationResultListenerList.contains(listener)) {
            Log.v(this.javaClass.name, "LocationResultListener is already registered.")
            return
        }

        mLocationResultListenerList.add(listener)
    }

    /**
     * 위치 리스너를 제거한다
     */
    fun removeLocationResultListener(listener: ILocationResultListener) {
        if(!mLocationResultListenerList.contains(listener)) {
            Log.v(this.javaClass.name, "There is no this listener in LocationResultListenerList: " + listener.hashCode())
            return
        }

        mLocationResultListenerList.remove(listener)
    }

    /**
     * 위치 리스너를 모두 제거한다
     */
    fun clearLocationResultListenerList() {
        Log.v(this.javaClass.name, "All LocationResultListeners are removed.")
        mLocationResultListenerList.clear()
    }

}