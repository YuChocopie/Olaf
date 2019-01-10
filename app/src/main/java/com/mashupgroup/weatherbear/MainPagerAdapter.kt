package com.mashupgroup.weatherbear

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mashupgroup.weatherbear.models.weather.Weather
import android.support.design.widget.CoordinatorLayout.Behavior.setTag
import android.util.Log
import com.mashupgroup.weatherbear.R.id.view
import com.mashupgroup.weatherbear.databinding.ItemWeatherBinding

class MainPagerAdapter : PagerAdapter {
    private var context: Context
    var itemList = ArrayList<MainPagerItem>(); private set

    constructor(context: Context) {
        this.context = context
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return itemList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                as LayoutInflater


        val binding:ItemWeatherBinding = DataBindingUtil.inflate(layoutInflater, R.layout
                .item_weather, container, false)
        binding.isDayData = itemList[position].vmInfo
        container.addView(binding.getRoot())


        //선그래프
        val dayTimeTemperture = intArrayOf(15, 3, 7, 8, -14, 1, 3, 6)
        //기존온도보다 30도 높게줬다..영하-50이여도 그릴 수 있게
        val plusTime = 50
        val LineTemperture= dayTimeTemperture
        var dayMaxTemperture = -100
        var dayMinTemperture = 100
        for (i in 0 until LineTemperture.size) {
            LineTemperture[i]=LineTemperture[i]+plusTime
            if(dayMaxTemperture<LineTemperture[i]) {
                dayMaxTemperture = LineTemperture[i]
            }
            if(dayMinTemperture>LineTemperture[i]) {
                dayMinTemperture = LineTemperture[i]
            }
        }


        val weekHighTempture= intArrayOf(5, 3, 7, 8, 5)
        val weekLowTempture= intArrayOf(0, -3, 0, 0, -3)
        //기존온도보다 30도 높게줬다..영하-50이여도 그릴 수 있게
        var weekMaxTemperture = -100
        var weekMinTemperture = 100
        for (i in 0 until weekHighTempture.size) {
            weekHighTempture[i]=weekHighTempture[i]
            weekLowTempture[i]=weekLowTempture[i]
            if(weekMaxTemperture<weekHighTempture[i]) {
                weekMaxTemperture = weekHighTempture[i]
            }
            if(weekMinTemperture>weekLowTempture[i]) {
                weekMinTemperture = weekLowTempture[i]
            }
        }

        val weekGraphView = binding.root.findViewById(R.id.GraphViewWeek) as? GraphViewWeek
        val dayGraphView = binding.root.findViewById(R.id.GraphViewTime) as? GraphViewDay
        Log.e("12313","111111"+dayGraphView.toString()+"222"+weekGraphView.toString())

        if (weekGraphView != null) {
            weekGraphView.setPoints(weekHighTempture, weekLowTempture,0.7, weekMaxTemperture,
                    weekMinTemperture)
            weekGraphView.drawForBeforeDrawView()
        }
        if (dayGraphView != null) {
            dayGraphView.setPoints(LineTemperture, 0.74, plusTime, dayMaxTemperture,
                    dayMinTemperture)
            dayGraphView.drawForBeforeDrawView()
        }







        return binding.getRoot()
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    /**
     * 어뎁터의 모든 데이터를 바꾼다. 기존의 데이터는 사라진다.
     * @param dataList 새로 바꿀 데이터 리스트
     */
    fun resetData(dataList: ArrayList<MainPagerItem>) {
        itemList = dataList
        notifyDataSetChanged()
    }

    /**
     * 현재 어뎁터에 1개 아이템을 추가한다.
     */
    fun addData(data: MainPagerItem) {
        itemList.add(data)
        notifyDataSetChanged()
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
}