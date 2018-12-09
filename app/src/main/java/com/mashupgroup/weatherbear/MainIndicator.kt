package com.mashupgroup.weatherbear

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout

class MainIndicator : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, int: Int) : super(context, attrs, int)

    private val dotDrawableSel = resources.getDrawable(R.drawable.indicator_dot_sel)!!
    private val dotDrawableUnSel = resources.getDrawable(R.drawable.indicator_dot_unsel)!!
    private val dotList = ArrayList<ImageView>()

    /**
     * 인디케이터를 초기화한다.
     * @param count 아이템 개수
     */
    fun initIndicator(count: Int) {
        dotList.clear()
        removeAllViews()
        for(i: Int in 1..count) {
            val ivDot = ImageView(context)
            ivDot.setImageDrawable(dotDrawableUnSel)
            ivDot.isEnabled = false
            ivDot.visibility = View.VISIBLE
            val params = LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
            ivDot.layoutParams = params
            addView(ivDot)
            dotList.add(ivDot)
        }
    }

    /**
     * 아이템을 선택했음을 인디케이터에게 알린다.
     * @param position 선택된 아이템 index
     */
    fun selectItem(position: Int) {
        var selectPos = position
        if(selectPos >= dotList.size) selectPos = dotList.size-1

        if(selectPos < 0) return

        for(i: Int in dotList.indices) {
            if(i == position) {
                dotList[i].setImageDrawable(dotDrawableSel)
            } else {
                dotList[i].setImageDrawable(dotDrawableUnSel)
            }
        }
    }
}