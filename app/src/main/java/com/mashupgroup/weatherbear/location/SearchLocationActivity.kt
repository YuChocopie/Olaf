package com.mashupgroup.weatherbear.location

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.databinding.DataBindingUtil
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.mashupgroup.weatherbear.Global
import com.mashupgroup.weatherbear.R
import com.mashupgroup.weatherbear.databinding.ActivitySearchLocationBinding
import com.mashupgroup.weatherbear.viewmodels.SearchItemViewModel
import kotlinx.android.synthetic.main.activity_search_location.*
import java.io.IOException
import java.util.*

class SearchLocationActivity : AppCompatActivity(),
        SearchLocationAdapter.Companion.ISearchResultItemClickListener,
        TextWatcher {
    lateinit var binding : ActivitySearchLocationBinding
    private val adapter = SearchLocationAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_location)

        rvSearchLocationResult.adapter = adapter

        btnSearchLocation.setOnClickListener { searchLocation(etSearchLocation.text.toString()) }
        btnFromGps.setOnClickListener{ searchFromGps() }

        // 키보드에서 엔터키를 치면 검색을 수행하도록
        etSearchLocation.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (v != null && actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchLocation(v.text.toString())
                return@OnEditorActionListener true
            }
            false
        })

        // 키보드에 글자가 바뀌면 검색을 수행하도록
        etSearchLocation.addTextChangedListener(this)
    }

    override fun onDestroy() {
        LocationHelper.removeLocationResultListener(locationListener)
        super.onDestroy()
    }

    private fun updateLayoutVisibility(letsSearch : Boolean, searchResult : Boolean, noResult : Boolean, searching : Boolean) {
        if(letsSearch) layoutLetsSearch.visibility = View.VISIBLE
        else layoutLetsSearch.visibility = View.GONE

        if(searchResult) rvSearchLocationResult.visibility = View.VISIBLE
        else rvSearchLocationResult.visibility = View.GONE

        if(noResult) layoutNoResult.visibility = View.VISIBLE
        else layoutNoResult.visibility = View.GONE

        if(searching) layoutSearching.visibility = View.VISIBLE
        else layoutSearching.visibility = View.GONE
    }

    /**
     * 주어진 주소로부터 위치를 검색하고, 결과가 있을 경우 리사이클러뷰에 결과를 넣는다
     */
    private fun searchLocation(text : String) {
        if(text.isNullOrEmpty()) return;

        updateLayoutVisibility(false, false, false, true)

        val geocoder : Geocoder = Geocoder(this, Locale.getDefault())
        var list : ArrayList<Address>? = null
        try {
            list = geocoder.getFromLocationName(text, 20) as ArrayList<Address>?
        } catch (e : IOException) {
            e.printStackTrace()
            Toast.makeText(this, getString(R.string.search_location_error), Toast.LENGTH_SHORT).show()
        }

        if(list == null || list.size == 0) {
            rvSearchLocationResult.visibility = View.GONE
            updateLayoutVisibility(false, false, true, false)
            return
        }

        val listViewModel = ArrayList<SearchItemViewModel>()
        for (addr in list) {
            listViewModel.add(SearchItemViewModel(Global.createLocationString(addr, true), addr))
        }
        adapter.setData(listViewModel)

        updateLayoutVisibility(false, true, false, false)
    }

    /** GPS를 이용한 현재 위치를 검색한다 */
    private fun searchFromGps() {
        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            if(which == DialogInterface.BUTTON_POSITIVE) {
                // 위치 요청 및 요청중 다이얼로그 띄우기
                LocationHelper.addLocationResultListener(locationListener)
                LocationHelper.requestLocation(this, false)
                AlertDialog.Builder(this@SearchLocationActivity)
                        .setMessage(getString(R.string.msg_acquiring_your_location_gps))
                        .setCancelable(false)
                        .setNegativeButton(R.string.cancel) { _, _ ->
                            LocationHelper.cancelRequestLocation()
                            LocationHelper.removeLocationResultListener(locationListener)
                        }
                        .show()
            }
        }

        AlertDialog.Builder(this@SearchLocationActivity)
                .setMessage(getString(R.string.msg_add_location_from_gps_q))
                .setPositiveButton(getString(R.string.yes), dialogClickListener)
                .setNegativeButton(getString(R.string.no), dialogClickListener)
                .setCancelable(false)
                .show()
    }

    private val locationListener = object : ILocationResultListener {
        override fun onLocationReady(location: Location?, address: Address?) {
            LocationHelper.removeLocationResultListener(this)
            if(address != null) {
                onResultItemClick(address)
            } else {
                Toast.makeText(this@SearchLocationActivity, getString(R.string.unable_get_locaiton), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResultItemClick(address: Address) {
        // 아이템이 선택되면 선택된 아이템 저장 후, 현재 액티비티를 끝내고 결과를 반환한다
        // 선택된 아이템 저장
        Global.addressList.add(address)
        Global.saveAddressList()

        // 결과 반환
        val resultIntent = Intent()
        resultIntent.putExtra("lat", address.latitude)
        resultIntent.putExtra("long", address.longitude)
        resultIntent.putExtra("name", address.getAddressLine(0))
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    override fun afterTextChanged(s: Editable?) {
        // do nothing
    }
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // do nothing
    }

    private val searchHandler = Handler()
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if(layoutNoResult.visibility == VISIBLE)
            updateLayoutVisibility(true, false, false, false)

        // 글자가 바뀔 때마다 실시간으로 검색한다
        searchHandler.removeCallbacksAndMessages(null)
        searchHandler.postDelayed({
            searchLocation(etSearchLocation.text.toString())
        }, 300) // 부하를 줄이기 위해 입력종료 후 잠깐 있다가 검색시작
    }
}
