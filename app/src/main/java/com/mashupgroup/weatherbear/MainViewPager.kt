package com.mashupgroup.weatherbear

import android.content.Context
import android.icu.util.Measure
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v4.view.ViewPager.OnPageChangeListener
import android.util.AttributeSet

class MainViewPager : ViewPager, ViewPager.OnAdapterChangeListener {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    /** 이게 false이면 뷰페이저 초기 세팅이 안됐다는 뜻, 모든 기능이 올바르게 작동할거라는 보장이 없다 */
    var isInitialized = false

    /** 인디케이터 뷰 (어떤 페이지인지 쩜쩜쩜으로 나타내는거), null일 수 있다 */
    var indicatorView : MainIndicator? = null; private set

    /** 커스텀 뷰페이져 초기화. 꼭 부르길
     * @param indicator 인디케이터 뷰 */
    fun initialize(indicator : MainIndicator?) {
        indicatorView = indicator
        addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                indicatorView?.selectItem(position)
            }
        })

        isInitialized = true
    }

    override fun onAdapterChanged(viewPager: ViewPager, oldAdapter: PagerAdapter?, newAdapter: PagerAdapter?) {
        if(indicatorView != null && newAdapter != null) {
            indicatorView?.initIndicator(newAdapter.count)
        }
    }

    /**
     * 연결된 MainIndicator가 있다면 그 인디케이터를 초기화한다.
     */
    fun initIndicator() {
        if(adapter != null) {
            indicatorView?.initIndicator(adapter!!.count)
            indicatorView?.selectItem(currentItem)
        }
    }

    // onMeasure를 오버라이드하여 뷰페이져의 height = wrap_content 가 잘 작동하도록 한다.
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var totalHeight = 0
        for(i : Int in 0 until childCount) {
            val child = getChildAt(i)
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
            var curHeight = child.measuredHeight
            if(curHeight > totalHeight) totalHeight = curHeight
        }

        var returnHeightMeasureSpec = heightMeasureSpec
        if (totalHeight != 0)
            returnHeightMeasureSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY)

        super.onMeasure(widthMeasureSpec, returnHeightMeasureSpec)
    }
}