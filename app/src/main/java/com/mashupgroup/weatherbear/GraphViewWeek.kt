package com.mashupgroup.weatherbear

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver


class GraphViewWeek(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var mTextPaint: Paint? = null
    private var mPointsHigh: IntArray? = null
    private var mPointsLow: IntArray? = null
    private var mPointX: IntArray? = null
    private var mPointYHigh: IntArray? = null
    private var mPointYLow: IntArray? = null
    private var mTextGap: Float = 0f
    private var mLinePaint: Paint? = null
    private var mControlLinePaint: Paint? = null
    private var mUnit: Double = 0.0
    private var mStart: Int = 0
    private var mDivide: Int = 0

    init {
        setTypes(context, attrs)
    }

    //그래프 옵션을 받는다
    private fun setTypes(context: Context, attrs: AttributeSet) {
        val types = context.obtainStyledAttributes(attrs, R.styleable.GraphView)

        mTextPaint = Paint()
        mTextPaint!!.color = types.getColor(R.styleable.GraphView_textColor, Color.BLACK)
        mTextPaint!!.textSize = types.getDimension(R.styleable.GraphView_textSize, 10f)
        mTextPaint!!.textAlign = Paint.Align.CENTER

        mTextGap = types.getDimension(R.styleable.GraphView_textGap, 0f)

        mLinePaint = Paint()
        mLinePaint!!.strokeWidth = types.getDimension(R.styleable.GraphView_lineThickness, 10f)
        mLinePaint!!.color = types.getColor(R.styleable.GraphView_lineColor, Color.GRAY)
        mControlLinePaint = Paint()
        mControlLinePaint!!.strokeWidth = 10f
        mControlLinePaint!!.color =Color.RED
    }

    //그래프 정보를 받는다
    fun setPoints(HighPoints: IntArray, LowPoints: IntArray, unit: Double, max: Int, min: Int) {
        mPointsHigh = HighPoints   //y축 값 배열
        mPointsLow = LowPoints   //y축 값 작은배열
        mUnit = unit       //y축 단위
        mStart = min   //y축 원점
        mDivide = max - min  //y축 값 갯수
    }

    //그래프를 만든다
    fun draw() {
        val height = height*0.89
        val hightPoints = mPointsHigh
        val lowPoints = mPointsLow

        //x축 점 사이의 거리
        val gapx = width.toFloat() / hightPoints!!.size

        //y축 단위 사이의 거리
        val gapy = (height / mDivide) * mUnit.toFloat()

        val halfgab = gapx / 2

        val length = hightPoints.size
        mPointX = IntArray(length)
        mPointYHigh = IntArray(length)
        mPointYLow = IntArray(length)
        Log.e("123123", IntArray(length).toString() + " ccc")

        for (i in 0 until length) {
            //막대 좌표를 구한다
            val x = (halfgab + i * gapx).toInt()
            val yHight = (height.toFloat() - (hightPoints[i] - mStart) * gapy).toInt()
            val yLow = (height.toFloat() - (lowPoints!![i] - mStart) * gapy).toInt()

            mPointX!![i] = x
            mPointYHigh!![i] = yHight
            mPointYLow!![i] = yLow

        }
    }

    //그래프를 그린다(onCreate 등에서 호출시)
    fun drawForBeforeDrawView() {
        //뷰의 크기를 계산하여 그래프를 그리기 때문에 뷰가 실제로 만들어진 시점에서 함수를 호출해 준다
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                draw()

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                    viewTreeObserver.removeGlobalOnLayoutListener(this)
                else
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mPointX != null && mPointYHigh != null && mPointYLow != null) {
            val length = mPointX!!.size
            for (i in 0 until length) {
                val x = mPointX!![i].toFloat()
                val yHigh = mPointYHigh!![i].toFloat()
                val yLow = mPointYLow!![i].toFloat()

                canvas.drawText("" + mPointsHigh!![i] + "°", x, yHigh-5, mTextPaint!!)
                canvas.drawText("" + mPointsLow!![i] + "°", x, yLow+30, mTextPaint!!)
                canvas.drawLine(x, yHigh, x, yLow, mLinePaint!!)
            }

        }
    }
}
