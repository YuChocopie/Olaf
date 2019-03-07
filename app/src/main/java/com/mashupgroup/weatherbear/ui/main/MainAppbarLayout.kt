package com.mashupgroup.weatherbear.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.support.design.widget.AppBarLayout
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.mashupgroup.weatherbear.R
import kotlinx.android.synthetic.main.top_bear.view.*

class MainAppbarLayout : AppBarLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    enum class STATE {EXPANDED, COLLAPSED, IDLE}
    private var currentState = STATE.EXPANDED

    @SuppressLint("PrivateResource")
    var animFadeIn = AnimationUtils.loadAnimation(context, R.anim.abc_fade_in)
    @SuppressLint("PrivateResource")
    var animFadeOut = AnimationUtils.loadAnimation(context, R.anim.abc_fade_out)

    init {
        animFadeIn.setAnimationListener(
            object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    bearWrapper.visibility = View.VISIBLE
                }
            }
        )

        animFadeOut.setAnimationListener(
            object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    bearWrapper.visibility = View.GONE
                }
            }
        )

        addOnOffsetChangedListener { _, verticalOffset ->
            run {
                when {
                    verticalOffset == 0 -> {
                        // Expanded
                        currentState = STATE.EXPANDED
                    }
                    Math.abs(verticalOffset) >= totalScrollRange -> {
                        // Collapsed
                        if(currentState != STATE.COLLAPSED) {
                            bearWrapper.startAnimation(animFadeOut)
                        }

                        currentState = STATE.COLLAPSED
                    }
                    else -> {
                        // Middle of scroll
                        if(currentState == STATE.COLLAPSED) {
                            bearWrapper.startAnimation(animFadeIn)
                        }

                        currentState = STATE.IDLE
                    }
                }
            }
        }
    }
}