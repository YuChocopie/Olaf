package com.mashupgroup.weatherbear

import android.content.Context
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet
import android.view.MotionEvent

class MainScrollView: NestedScrollView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var lastX = 0f
    private var lastY = 0f

    // 스크롤이 시작될 때 손가락 터치 이동방향을 계산해서 상하면 상하스크롤, 좌우면 뷰페이져스크롤되도록 범위를 조절
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        ev?.let {
            when(it.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = it.x
                    lastY = it.y
                }
                MotionEvent.ACTION_MOVE -> {
                    return Math.abs(it.x - lastX) < Math.abs(it.y - lastY)
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }
}