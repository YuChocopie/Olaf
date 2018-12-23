package com.mashupgroup.weatherbear.location

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mashupgroup.weatherbear.Global
import com.mashupgroup.weatherbear.R
import com.mashupgroup.weatherbear.viewmodels.LocalViewModel
import kotlinx.android.synthetic.main.activity_select_location.*

class SelectLocationActivity : AppCompatActivity() {
    val RESULT_CODE_SEARCH_LOCATION_ACTIVITY = 111

    private val longListener = object : SelectLocationAdapter.Companion.ISelectLocationItemListener {
        override fun onLongPressItem() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }
    private val mAdapter = SelectLocationAdapter(longListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_location)

        fabtnAddLocation.setOnClickListener {onSearchLocationFABtnClicked() }

        // 저장됐었던 위치 정보들을 불러온다
        loadAndShowLocationsInfo()
        rvLocationList.adapter = mAdapter
    }

    private fun onSearchLocationFABtnClicked() {
        val intent = Intent(this, SearchLocationActivity::class.java)
        startActivityForResult(intent, RESULT_CODE_SEARCH_LOCATION_ACTIVITY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == RESULT_CODE_SEARCH_LOCATION_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK) {
                // 검색 성공 및 결과 받아옴 (data intent안에 결과 있어야함)
                data?.let { intent ->
                    val lat = intent.getDoubleExtra("lat", .0)
                    val long = intent.getDoubleExtra("long", .0)
                    val name = intent.getStringExtra("name")
                    Log.v("JUJINTEST", "$name, $lat, $long")
                }
            } else {
                // 검색 실패 또는 취소.
            }

            loadAndShowLocationsInfo()
        }
    }

    fun loadAndShowLocationsInfo() {
        val locationList = ArrayList<LocalViewModel>()
        for(address in Global.addressList) {
            val newData = LocalViewModel()
            if(address.maxAddressLineIndex >= 0)
                newData.localName = address.getAddressLine(0)
            locationList.add(newData)
        }

        mAdapter.setData(locationList)
    }
}
