package com.mashupgroup.weatherbear.location

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.mashupgroup.weatherbear.R
import com.mashupgroup.weatherbear.databinding.ActivitySearchLocationBinding
import kotlinx.android.synthetic.main.activity_search_location.*

class SearchLocationActivity : AppCompatActivity() {
    lateinit var binding : ActivitySearchLocationBinding
    val adapter = SearchLocationAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_location)

        rvSearchLocationResult.adapter = adapter

        val list = ArrayList<SearchItemViewModel>()
        list.add(SearchItemViewModel("1111"))
        list.add(SearchItemViewModel("234"))
        list.add(SearchItemViewModel("ffss"))
        adapter.setData(list)
    }
}
