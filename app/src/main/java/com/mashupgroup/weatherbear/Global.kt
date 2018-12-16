package com.mashupgroup.weatherbear

import android.content.Context.MODE_PRIVATE
import android.location.Address
import android.location.Geocoder
import java.util.*

const val SP_WEATHERBEAR_KEY = "WeatherBearKey"
const val SP_ADDR_SAVE_KEY = "AddressSaveKey"
const val SP_SEETING_SAVE_KEY = "SeetingSaveKey"

/**
 * 모든 클래스에서 참조할 수 있는 도우미 싱글톤 클래스
 */
object Global {
    var addressList = ArrayList<Address>()

    /**
     * SharedPref를 사용하여 사용자가 설정한 위치 목록을 핸드폰에 저장
     * @param addrList 위치 목록
     */
    fun saveAddressList() {
        val context = WeatherBearApp.appContext
        val prefs = context.getSharedPreferences(SP_WEATHERBEAR_KEY, MODE_PRIVATE)
        val editor = prefs.edit()

        val addrSet = HashSet<String>()
        for (addr in addressList) {
            val strLatLong = String.format("%f|%f", addr.latitude, addr.longitude)
            addrSet.add(strLatLong)
        }

        editor.putStringSet(SP_ADDR_SAVE_KEY, addrSet)

        editor.apply()
    }

    /**
     * SharedPref로부터 핸드폰에 저장된 사용자의 위치 목록을 불러옴
     * @return 저장된 위치 목록
     */
    fun loadAddressList() : List<Address>? {
        val context = WeatherBearApp.appContext
        val geocoder = Geocoder(context, Locale.getDefault())

        val prefs = context.getSharedPreferences(SP_WEATHERBEAR_KEY, MODE_PRIVATE)
        val savedStrSet = prefs.getStringSet(SP_ADDR_SAVE_KEY, null) ?: return null

        val lstAddr = ArrayList<Address>()

        for(set in savedStrSet) {
            val lat0long1 = set.split('|')
            val savedAddr = geocoder.getFromLocation(
                    lat0long1[0].toDouble(),
                    lat0long1[1].toDouble(),
                    1
            )

            if(savedAddr.size > 0) {
                lstAddr.add(savedAddr[0])
            }
        }

        addressList = lstAddr
        return lstAddr
    }

    /**
     * Address객체를 주소 string (가장 근접한애)으로 변경
     */
    fun createLocationString(address : Address) : String {
        var result = "No result"
        if(address.maxAddressLineIndex > 0) {
            result = address.getAddressLine(0)
        }
        return result
    }
}