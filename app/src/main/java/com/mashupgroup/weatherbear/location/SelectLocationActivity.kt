package com.mashupgroup.weatherbear.location

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.location.Address
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mashupgroup.weatherbear.Global
import com.mashupgroup.weatherbear.R
import com.mashupgroup.weatherbear.viewmodels.LocalViewModel
import kotlinx.android.synthetic.main.activity_select_location.*

class SelectLocationActivity : AppCompatActivity() {
    val RESULT_CODE_SEARCH_LOCATION_ACTIVITY = 111

    /** 위치 리스트의 한 아이템이 오래 눌렸을 때 불리는 콜백리스너 */
    private val longListener = object : SelectLocationAdapter.Companion.ISelectLocationItemListener {
        override fun onLongPressItem(address: Address) {
            // 아이템이 길게 눌림 -> 아이템 삭제할지 물어보고 삭제
            addressToDelete = address
            AlertDialog.Builder(this@SelectLocationActivity)
                    .setMessage(getString(R.string.msg_delete_this_address_q))
                    .setPositiveButton(getString(R.string.yes), dialogClickListener)
                    .setNegativeButton(getString(R.string.no), dialogClickListener)
                    .show()
        }
    }

    private var addressToDelete: Address? = null
    /** 리스트의 아이템 삭제 다이얼로그에서 예/아니오 눌렀을 때 불리는 콜백리스너 */
    private val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
        if(which == DialogInterface.BUTTON_POSITIVE) {
            if(addressToDelete == null) return@OnClickListener

            Global.addressList.remove(addressToDelete!!)
            Global.saveAddressList()
            loadAndShowLocationsInfo()
        }

        addressToDelete = null
    }

    private val mAdapter = SelectLocationAdapter(longListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_location)

        // '위치 추가' floating action button 클릭시 리스너 등록
        fabtnAddLocation.setOnClickListener {onSearchLocationFABtnClicked() }

        // '첫 페이지 현재 위치?' 체크 불러와서 체크처리하기
        chkAddCurrentLocation.isChecked = Global.isFirstPageCurrentLocation
        // '첫 페이지 현재 위치?' 체크 변경시 리스너 등록
        chkAddCurrentLocation.setOnCheckedChangeListener { buttonView, isChecked ->
            // '첫 페이지에 현재 위치 표시' 체크가 바뀔 때마다 저장한다
            Global.isFirstPageCurrentLocation = isChecked
            Global.saveIsFirstPageCurrentLocation()
         }

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

    private fun loadAndShowLocationsInfo() {
        val locationList = ArrayList<SelectLocationItem>()
        for(address in Global.addressList) {
            val newData = SelectLocationItem(LocalViewModel(), address)
            if(address.maxAddressLineIndex >= 0)
                newData.viewModel.localName = Global.createLocationString(address)
            locationList.add(newData)
        }

        mAdapter.setData(locationList)
    }
}
