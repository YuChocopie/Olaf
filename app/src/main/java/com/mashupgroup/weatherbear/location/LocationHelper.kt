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
import java.util.Locale

import android.content.Context.LOCATION_SERVICE
import com.mashupgroup.weatherbear.R
import com.mashupgroup.weatherbear.WeatherBearApp

/** jujin.kim
 * Location Helper
 * 지역 정보를 얻어오는 클래스
 *
 * 생성 후 requestLocation로 위치 요청 및 콜백메서드를 통해 위치 요청 결과를 받아올 수 있다.
 * 사용을 위해 COARSE & FINE LOCATION 권한이 반드시 필요하며 스플래시 스크린에서 꼭 권한 따길 바람.
 */
class LocationHelper {

    lateinit var lastLocation: Location; private set
    private var mLocationManager: LocationManager =
            WeatherBearApp.appContext.getSystemService(LOCATION_SERVICE) as LocationManager
    private var locationInformed: Boolean = false

    private var mLocationReadyListener: ILocationResultReceiver? = null

    private val mLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            lastLocation = location
            if (mLocationReadyListener != null && !locationInformed) {
                mLocationReadyListener!!.onLocationReady(locationAddresses)
                locationInformed = true
            }

            mLocationManager.removeUpdates(this)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

        override fun onProviderEnabled(provider: String) {}

        override fun onProviderDisabled(provider: String) {}
    }

    /**
     * 마지막 위치 정보를 이용하여 현재 접속중인 위치주소 정보를 알아온다.
     * GeoCoder를 이용한다
     * @return 위치 주소 LIST (없을경우 null)
     */
    val locationAddresses: List<Address>?
        get() {
            val context = WeatherBearApp.appContext
            val geocoder = Geocoder(context, Locale.getDefault())
            try {
                return geocoder.getFromLocation(
                        lastLocation.latitude,
                        lastLocation.longitude, 1
                )
            } catch (ioException: IOException) {
                ioException.printStackTrace()
            } catch (exception : Exception) {
                exception.printStackTrace()
            }

            return null
        }

    /**
     * 위치를 1회 요청한다. 요청 성공시 위치 반환 콜백을 준 후 리스너 제거함 (오직 1회 요청)
     * @param activityContext
     * Activity context
     * @param listener
     * 요청 성공 및 위치 받아왔을 때 콜백. List<Address>를 매개변수로 콜백 호출한다.
     * getCountryName으로 나라 이름, getLocality로 지역 이름 등을 가져올 수 있다.
     * @return 요청 실패시(권한X, 또는 이미 요청중) false, 성공시 true 반환
     */
    fun requestLocation(activityContext: Activity, listener: ILocationResultReceiver): Boolean {
        if (mLocationReadyListener != null) {
            Log.v(this.javaClass.name, "Requesting location is already in progress.")
            return false
        }

        mLocationReadyListener = listener
        mLocationManager = activityContext.getSystemService(LOCATION_SERVICE) as LocationManager

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
        mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, LOCATION_REFRESH_PEROID.toLong(),
                LOCATION_REFRESH_DIST.toFloat(), mLocationListener
        )
        mLocationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, LOCATION_REFRESH_PEROID.toLong(),
                LOCATION_REFRESH_DIST.toFloat(), mLocationListener
        )

        return true
    }

    companion object {
        private const val LOCATION_REFRESH_PEROID = 1000
        private const val LOCATION_REFRESH_DIST = 1
    }

}