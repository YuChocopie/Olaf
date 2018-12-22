package com.mashupgroup.weatherbear.location

import android.content.Intent
import android.databinding.DataBindingUtil
import android.location.Address
import android.location.Geocoder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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

class SearchLocationActivity : AppCompatActivity() {
    lateinit var binding : ActivitySearchLocationBinding
    private var onResultClickListener = object : SearchLocationAdapter.Companion.ISearchResultItemClickListener {
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
    }
    private val adapter = SearchLocationAdapter(onResultClickListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_location)

        rvSearchLocationResult.adapter = adapter

        btnSearchLocation.setOnClickListener { searchLocation(etSearchLocation.text.toString()) }
        etSearchLocation.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            // 키보드에서 엔터키를 치면 검색을 수행하도록
            if (v != null && actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchLocation(v.text.toString())
                return@OnEditorActionListener true
            }
            false
        })
    }

    fun updateLayoutVisibility(letsSearch : Boolean, searchResult : Boolean, noResult : Boolean, searching : Boolean) {
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
    fun searchLocation(text : String) {
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
            listViewModel.add(SearchItemViewModel(addr.getAddressLine(0), addr))
        }
        adapter.setData(listViewModel)

        updateLayoutVisibility(false, true, false, false)
    }
}
