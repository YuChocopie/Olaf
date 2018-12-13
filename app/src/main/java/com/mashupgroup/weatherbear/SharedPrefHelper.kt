package com.mashupgroup.weatherbear

import android.content.Context.MODE_PRIVATE
import android.location.Address
import android.location.Geocoder
import java.util.*

const val SP_WEATHERBEAR_KEY = "WeatherBearKey"
const val SP_ADDR_SAVE_KEY = "AddressSaveKey"
const val SP_SEETING_SAVE_KEY = "SeetingSaveKey"

/**
 * SharedPreferences를 이용하여 설정을 저장하고 불러오는 도우미 싱글톤 클래스
 */
object SharedPrefHelper {

    /**
     * 사용자가 설정한 위치 목록을 핸드폰에 저장
     * @param addrList 위치 목록
     */
    fun saveAddressList(addrList : List<Address>) {
        val context = WeatherBearApp.appContext
        val prefs = context.getSharedPreferences(SP_WEATHERBEAR_KEY, MODE_PRIVATE)
        val editor = prefs.edit()

        val addrSet = HashSet<String>()
        for (addr in addrList) {
            val strLatLong = String.format("%f|%f", addr.latitude, addr.longitude)
            addrSet.add(strLatLong)
        }

        editor.putStringSet(SP_ADDR_SAVE_KEY, addrSet)

        editor.apply()
    }

    /**
     * 핸드폰에 저장된 사용자의 위치 목록을 불러옴
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
                    0
            )
            lstAddr.add(savedAddr[0])
        }

        return lstAddr
    }
}