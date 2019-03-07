package com.mashupgroup.weatherbear

import android.content.Context.MODE_PRIVATE
import android.location.Address
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Exception

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
        editor.putString(SP_ADDR_SAVE_KEY, Gson().toJson(addressList))

        editor.apply()
    }

    /**
     * SharedPref로부터 핸드폰에 저장된 사용자의 위치 목록을 불러옴
     * @return 저장된 위치 목록
     */
    fun loadAddressList() : List<Address>? {
        addressList.clear()
        val context = WeatherBearApp.appContext

        try {
            val prefs = context.getSharedPreferences(SP_WEATHERBEAR_KEY, MODE_PRIVATE)
            val addrJsonArrString = prefs.getString(SP_ADDR_SAVE_KEY, null) ?: return addressList
            addressList = Gson().fromJson(addrJsonArrString, (object: TypeToken<ArrayList<Address>>(){}).type)
        } catch (e : Exception) {
            e.printStackTrace()
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
     * 19.01.14 : 실시간 위치 받아오는건 법률적 이슈(위치기반서비스신고)도 있고 해서, 일단은 TBD (To be decision) 상태로 보류. 대신, 위치 검색시 '현재 위치 추가' 기능을 넣기로
     */
    fun loadIsFirstPageCurrentLocation() {
        isFirstPageCurrentLocation = false  // FirstPage Current Location : TBD
        /*
        val context = WeatherBearApp.appContext
        val prefs = context.getSharedPreferences(SP_WEATHERBEAR_KEY, MODE_PRIVATE)
        isFirstPageCurrentLocation = prefs.getBoolean(SP_FIRSTSCREEN_CURRENTLOC_SAVE_KEY, false)
        */
    }

    /**
     * Address객체를 주소 string (가장 근접한애)으로 변경
     */
    fun createLocationString(address : Address, isFull: Boolean) : String {
        var result = "No result"
        val stringBuilder = StringBuilder()

        if(address.maxAddressLineIndex >= 0) {
            if(isFull) {
                if (!address.countryName.isNullOrEmpty())
                    stringBuilder.append(address.countryName)
                if (!stringBuilder.isEmpty()) stringBuilder.append('\n')
            }

            if (!address.adminArea.isNullOrEmpty() && address.adminArea != address.countryName)
                stringBuilder.append(' ').append(address.adminArea)
            if(!address.subAdminArea.isNullOrEmpty() && address.subAdminArea != address.adminArea)
                stringBuilder.append(' ').append(address.subAdminArea)
            if(!address.locality.isNullOrEmpty() && address.locality != address.subAdminArea)
                stringBuilder.append(' ').append(address.locality)
            if(!address.subLocality.isNullOrEmpty() && address.subLocality != address.locality)
                stringBuilder.append(' ').append(address.subLocality)

            if(!address.thoroughfare.isNullOrEmpty() && address.thoroughfare != address.subLocality)
                stringBuilder.append(' ').append(address.thoroughfare)
            else if(!address.featureName.isNullOrEmpty() &&
                    address.featureName != address.thoroughfare &&
                    address.featureName != address.subLocality &&
                    address.featureName != address.locality)
                stringBuilder.append(' ').append(address.featureName)
            //if(!address.subThoroughfare.isNullOrEmpty()) stringBuilder.append(' ').append(address.subThoroughfare)
            result = stringBuilder.toString()
        }
        return result
    }

    /**
        address 객체가 가지고있는 가장 작은 행정지역 단위 문자열을 가져옴(단, 최소 throughfare)
     */
    fun getSmallestLocationString(address: Address): String {
        if(!address.thoroughfare.isNullOrEmpty()) return address.thoroughfare
        if(!address.subLocality.isNullOrEmpty()) return address.subLocality
        if(!address.locality.isNullOrEmpty()) return address.locality
        if(!address.subAdminArea.isNullOrEmpty()) return address.subAdminArea
        if(!address.adminArea.isNullOrEmpty()) return address.adminArea
        if(!address.countryName.isNullOrEmpty()) return address.countryName
        if(!address.featureName.isNullOrEmpty()) return address.featureName // feature는 정말 최소행정단위인데, 가끔 '북극'같은경우 feature만 있는경우가있어서..
        return "No data"
    }
}