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
import com.mashupgroup.weatherbear.R.id.view


class MainPagerAdapter : PagerAdapter {
    private var context: Context
    // Todo : 아이템 타입을 Weather로 해도 될지 모르겠다. 될 것 같긴한데..만약 안될 경우 적절한 타입으로 바꿀 것
    private var itemList = ArrayList<Weather>()

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
        var layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view = layoutInflater.inflate(R.layout.item_weather, container, false)


        var binding: ViewDataBinding = DataBindingUtil.inflate(layoutInflater, R.layout
                .item_weather, container,
                false)
        binding.getRoot().setTag(position)
        container.addView(binding.getRoot())
        return binding.getRoot()



//        container.addView(view)
//        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    /**
     * 어뎁터의 모든 데이터를 바꾼다. 기존의 데이터는 사라진다.
     * @param dataList 새로 바꿀 데이터 리스트
     */
    fun resetData(dataList: ArrayList<Weather>) {
        itemList = dataList
        notifyDataSetChanged()
    }

    /**
     * 현재 어뎁터에 1개 아이템을 추가한다.
     */
    fun addData(data: Weather) {
        itemList.add(data)
        notifyDataSetChanged()
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
}