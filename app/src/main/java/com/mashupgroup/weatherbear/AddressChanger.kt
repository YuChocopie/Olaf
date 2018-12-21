package com.mashupgroup.weatherbear

import android.content.Context
import android.location.Geocoder
import android.util.Log
import java.io.IOException
import java.util.*

class AddressChanger {
    /** (위도,경도) -> (도시명) */
    fun getStateAddress(context: Context, LATITUDE: Double, LONGITUDE: Double) : String {
        Log.d(this.javaClass.name, "AddressChanger")
        var ret = "Error"

        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
            if (addresses != null && addresses.size > 0) {
                val city = addresses[0].locality
                val state = addresses[0].adminArea

                val result = when(state) {
                    "서울특별시" -> "서울"
                    "부산광역시" -> "부산"
                    "대구광역시" -> "대구"
                    "인천광역시" -> "인천"
                    "광주광역시" -> "광주"
                    "대전광역시" -> "대전"
                    "울산광역시" -> "울산"
                    "경기도" -> "경기"
                    "강원도" -> "강원"
                    "충청북도" -> "충북"
                    "충청남도" -> {
                        if(city=="연기군")  // (세종시도 충청남도로 넘어옴)
                            "세종"
                        else
                            "충남"
                    }
                    "전라북도" -> "전북"
                    "전라남도" -> "전남"
                    "경상북도" -> "경북"
                    "경상남도" -> "경남"
                    "제주특별자치도" -> "제주"
                    else -> "Error"
                }
                ret = result
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ret
    }
}