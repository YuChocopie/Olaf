package com.mashupgroup.weatherbear.location

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.location.Address
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import com.mashupgroup.weatherbear.Global
import com.mashupgroup.weatherbear.R
import com.mashupgroup.weatherbear.viewmodels.LocalViewModel
import kotlinx.android.synthetic.main.activity_select_location.*

class SelectLocationActivity : AppCompatActivity() {
    private val RESULT_CODE_SEARCH_LOCATION_ACTIVITY = 111
    private var isItemChanged = false

    /** 위치 리스트의 아이템이 변경되었을 때(swipe/reorder) 불리는 콜백리스너 */
    private val changedListener: SelectLocationAdapter.Companion.IItemChangedRequestListener =
            object : SelectLocationAdapter.Companion.IItemChangedRequestListener {

        // 삭제요청시 삭제할지 먼저 물어봄
        override fun onRequestedItemRemove(address: Address) {
            addressToDelete = address
            AlertDialog.Builder(this@SelectLocationActivity)
                    .setMessage(getString(R.string.msg_delete_this_address_q))
                    .setPositiveButton(getString(R.string.yes), dialogClickListener)
                    .setNegativeButton(getString(R.string.no), dialogClickListener)
                    .show()
        }

        // 순서바뀐거라면 걍 순서바꾸고 저장
        override fun onRequestedItemSwap() {
            // 리스트 읽어와서 주소 싹다 다시저장
            Global.addressList.clear()
            for(addressItem: SelectLocationItem in mAdapter.itemList) {
                Global.addressList.add(addressItem.address)
            }
            Global.saveAddressList()
            isItemChanged = true
        }
    }

    private var addressToDelete: Address? = null
    /** 리스트의 아이템 삭제 다이얼로그에서 예/아니오 눌렀을 때 불리는 콜백리스너 */
    private val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
        if(which == DialogInterface.BUTTON_POSITIVE) {
            if(addressToDelete == null) return@OnClickListener

            Global.addressList.remove(addressToDelete!!)
            Global.saveAddressList()
            isItemChanged = true
        }
        loadAndShowLocations()
        addressToDelete = null
    }

    private val mAdapter = SelectLocationAdapter(changedListener)
    private val mItemTouchHelper = SelectLocationItemTouchCallback(mAdapter)

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
            isItemChanged = true
         }

        // 저장됐었던 위치 정보들을 불러온다
        loadAndShowLocations()
        rvLocationList.adapter = mAdapter

        // 드래그시 위치 변경 헬퍼를 새로 만들어서 붙인다
        ItemTouchHelper(mItemTouchHelper).attachToRecyclerView(rvLocationList)

        // 만약 위치 아이템이 한개도 없으면, 바로 위치 추가 액티비티를 띄운다
        if(mAdapter.itemList.size == 0) {
            val intent = Intent(this, SearchLocationActivity::class.java)
            startActivityForResult(intent, RESULT_CODE_SEARCH_LOCATION_ACTIVITY)
        }
    }

    override fun onBackPressed() {
        // 종료시 데이터 변경 여부에 대해 intent에 실어놓는다
        val resultIntent = Intent()
        resultIntent.putExtra("isItemChanged", isItemChanged)
        setResult(RESULT_OK, resultIntent)

        super.onBackPressed()
    }

    private fun onSearchLocationFABtnClicked() {
        val intent = Intent(this, SearchLocationActivity::class.java)
        startActivityForResult(intent, RESULT_CODE_SEARCH_LOCATION_ACTIVITY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RESULT_CODE_SEARCH_LOCATION_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK) {
                // 검색 성공 및 결과 받아옴 (data intent안에 결과 있어야함)
                data?.let { intent ->
                    val lat = intent.getDoubleExtra("lat", .0)
                    val long = intent.getDoubleExtra("long", .0)
                    val name = intent.getStringExtra("name")
                    isItemChanged = true
                    Log.v("JUJINTEST", "$name, $lat, $long")
                }
            } else {
                // 검색 실패 또는 취소.
            }

            loadAndShowLocations()
        }
    }

    private fun loadAndShowLocations() {
        val locationList = ArrayList<SelectLocationItem>()
        for(address in Global.addressList) {
            val newData = SelectLocationItem(LocalViewModel(Global.createLocationString(address, true)), address)
            if(address.maxAddressLineIndex >= 0)
            locationList.add(newData)
        }

        mAdapter.setData(locationList)
    }
}
