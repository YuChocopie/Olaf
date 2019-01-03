package com.mashupgroup.weatherbear

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.PathShape
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver


class GraphViewDay(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var mLineShape: ShapeDrawable? = null
    private var mTextPaint: Paint? = null
    private var mPointPaint: Paint? = null
    private var nPointPaint: Paint? = null
    private var mThickness: Float = 0.toFloat()
    private var mPoints: IntArray? = null
    private var mPointX: IntArray? = null
    private var mPointY: IntArray? = null
    private var mTextGap: Float = 0f
    private var mPointSize: Int = 0
    private var mPointRadius: Int = 0
    private var mLineColor: Int = 0
    private var mUnit: Double = 0.0
    private var mStart: Int = 0
    private var mOrigin: Int = 0
    private var mDivide: Int = 0

    init {

        setTypes(context, attrs)
    }

    //그래프 옵션을 받는다
    private fun setTypes(context: Context, attrs: AttributeSet) {
        val types = context.obtainStyledAttributes(attrs, R.styleable.GraphView)

        nPointPaint = Paint()
        nPointPaint!!.style = Paint.Style.FILL
        nPointPaint!!.color = types.getColor(R.styleable.GraphView_pointColor, Color.WHITE)
        mPointPaint = Paint()
        mPointPaint!!.style = Paint.Style.STROKE
        mPointPaint!!.strokeWidth = 5f
        mPointPaint!!.color = types.getColor(R.styleable.GraphView_pointColor, Color.GRAY)

        mPointSize = types.getDimension(R.styleable.GraphView_pointSize, 10f).toInt()
        mPointRadius = mPointSize / 2

        mTextPaint = Paint()
        mTextPaint!!.color = types.getColor(R.styleable.GraphView_textColor, Color.BLACK)
        mTextPaint!!.textSize = types.getDimension(R.styleable.GraphView_textSize, 0f)
        mTextPaint!!.textAlign = Paint.Align.CENTER

        mTextGap = types.getDimension(R.styleable.GraphView_textGap, 0f)


        mLineColor = types.getColor(R.styleable.GraphView_lineColor, Color.BLACK)
        mThickness = types.getDimension(R.styleable.GraphView_lineThickness, 1f)
    }

    //그래프 정보를 받는다
    fun setPoints(points: IntArray, unit: Double, origin: Int, max: Int, min: Int) {
        mPoints = points   //y축 값 배열
        mUnit = unit       //y축 단위
        mOrigin = origin   //y축 증가값
        mStart = min   //y축 원점
        mDivide = max - min   //y축 값 갯수
    }

    //그래프를 만든다
    fun draw() {
        val path = Path()

        val height = height
        val points = mPoints

        //x축 점 사이의 거리
        val gapx = width.toFloat() / points!!.size

        //y축 단위 사이의 거리
        val gapy = (height.toFloat() - mPointSize) / mDivide * mUnit.toFloat()

        val halfgab = gapx / 2

        val length = points.size
        mPointX = IntArray(length)
        mPointY = IntArray(length)

        for (i in 0 until length) {
            //점 좌표를 구한다
            val x = (halfgab + i * gapx).toInt()
            val y = (height.toFloat() - mPointRadius.toFloat() - (points[i] - mStart) * gapy).toInt()

            mPointX!![i] = x
            mPointY!![i] = y

            //선을 그린다
            if (i == 0)
                path.moveTo(x.toFloat(), y.toFloat())
            else
                path.lineTo(x.toFloat(), y.toFloat())
        }

        //그려진 선으로 shape을 만든다
        val shape = ShapeDrawable(PathShape(path, 1f, 1f))
        shape.setBounds(0, 0, 1, 1)

        val paint = shape.paint
        paint.style = Paint.Style.STROKE
        paint.color = mLineColor
        paint.strokeWidth = mThickness
        paint.isAntiAlias = true

        mLineShape = shape
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

        //선을 그린다
        if (mLineShape != null)
            mLineShape!!.draw(canvas)

        //점을 그린다
        if (mPointX != null && mPointY != null) {
            val length = mPointX!!.size

            for (i in 0 until length) {
                canvas.drawText(
                    "" + (mPoints!![i] - mOrigin), mPointX!![i].toFloat(),
                    mPointY!![i].toFloat() - mTextGap, mTextPaint!!
                )
                canvas.drawCircle(mPointX!![i].toFloat(), mPointY!![i].toFloat(), mPointRadius.toFloat(), nPointPaint!!)
                canvas.drawCircle(mPointX!![i].toFloat(), mPointY!![i].toFloat(), mPointRadius.toFloat(), mPointPaint!!)
            }
        }
    }
}
