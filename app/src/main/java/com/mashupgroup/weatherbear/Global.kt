package com.mashupgroup.weatherbear

import android.content.Context.MODE_PRIVATE
import android.location.Address
import android.location.Geocoder
import com.google.gson.Gson
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import kotlin.collections.LinkedHashSet

const val SP_WEATHERBEAR_KEY = "WeatherBearKey"
const val SP_ADDR_SAVE_KEY = "AddressSaveKey"
const val SP_FIRSTSCREEN_CURRENTLOC_SAVE_KEY = "FirstScreenCurrentLocationSaveKey"

/**
 * 모든 클래스에서 참조할 수 있는 도우미 싱글톤 클래스
 */
object Global {
    var addressList = ArrayList<Address>()
    var isFirstPageCurrentLocation = false

    /**
     * SharedPref를 사용하여 사용자가 설정한 위치 목록을 핸드폰에 저장
     * @param addrList 위치 목록
     */
    fun saveAddressList() {
        val context = WeatherBearApp.appContext
        val prefs = context.getSharedPreferences(SP_WEATHERBEAR_KEY, MODE_PRIVATE)
        val editor = prefs.edit()

        // gson을 이용하여 addressList를 각각 json으로 직렬화한 후, 직렬화된 문자열을 저장한다
        val gson = Gson()
        val saveJsonSet = LinkedHashSet<String>()
        for(address in addressList) {
            val addrJson = gson.toJson(address)
            saveJsonSet.add(addrJson)
        }

        editor.putStringSet(SP_ADDR_SAVE_KEY, saveJsonSet)

        editor.apply()
    }

    /**
     * SharedPref로부터 핸드폰에 저장된 사용자의 위치 목록을 불러옴
     * @return 저장된 위치 목록
     */
    fun loadAddressList() : List<Address>? {
        addressList.clear()
        val context = WeatherBearApp.appContext

        val prefs = context.getSharedPreferences(SP_WEATHERBEAR_KEY, MODE_PRIVATE)
        val addrJsonSet = prefs.getStringSet(SP_ADDR_SAVE_KEY, null) ?: return addressList

        val gson = Gson()
        for(addrJson in addrJsonSet) {
            try {
                val addr = gson.fromJson(addrJson, Address::class.java) ?: continue
                addressList.add(addr)
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }

        return addressList
    }

    /**
     * SharedPref에 '첫번째 페이지에 현재 위치 표시' 여부 저장 (Global.isFirstPageCurrentLocation 값 저장)
     */
    fun saveIsFirstPageCurrentLocation() {
        val context = WeatherBearApp.appContext
        val prefs = context.getSharedPreferences(SP_WEATHERBEAR_KEY, MODE_PRIVATE)
        val editor = prefs.edit()

        editor.putBoolean(SP_FIRSTSCREEN_CURRENTLOC_SAVE_KEY, isFirstPageCurrentLocation)
        editor.apply()
    }

    /**
     * SharedPref에서 '첫번째 페이지에 현재 위치 표시' 여부 불러옴 (Global.isFirstPageCurrentLocation 에 불러옴)
     */
    fun loadIsFirstPageCurrentLocation() {
        val context = WeatherBearApp.appContext
        val prefs = context.getSharedPreferences(SP_WEATHERBEAR_KEY, MODE_PRIVATE)
        isFirstPageCurrentLocation = prefs.getBoolean(SP_FIRSTSCREEN_CURRENTLOC_SAVE_KEY, false)
    }

    /**
     * Address객체를 주소 string (가장 근접한애)으로 변경
     */
    fun createLocationString(address : Address) : String {
        var result = "No result"
        val stringBuilder = StringBuilder()

        if(address.maxAddressLineIndex >= 0) {
            if(!address.countryName.isNullOrEmpty()) stringBuilder.append(address.countryName)
            if(!address.adminArea.isNullOrEmpty()) stringBuilder.append(' ').append(address.adminArea)
            if(!stringBuilder.isEmpty()) stringBuilder.append('\n')

            if(!address.subAdminArea.isNullOrEmpty()) stringBuilder.append(' ').append(address.subAdminArea)
            if(!address.locality.isNullOrEmpty()) stringBuilder.append(' ').append(address.locality)
            if(!address.subLocality.isNullOrEmpty()) stringBuilder.append(' ').append(address.subLocality)

            if(!address.thoroughfare.isNullOrEmpty()) stringBuilder.append(' ').append(address.thoroughfare)
            else if(!address.featureName.isNullOrEmpty()) stringBuilder.append(' ').append(address.featureName)
            //if(!address.subThoroughfare.isNullOrEmpty()) stringBuilder.append(' ').append(address.subThoroughfare)
            result = stringBuilder.toString()
        }
        return result
    }
}