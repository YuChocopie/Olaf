package com.mashupgroup.weatherbear

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.bear.view.*
import kotlinx.android.synthetic.main.item_bear_background.view.*

object BearAnimator {
    //눈사람
    val snow_bear: Animation
    // 고옴
    val bear_ear_r: Animation
    val bear_ear_l: Animation
    val leg_frong: Animation
    val leg_frong_good: Animation
    val leg_frong_bad: Animation
    val umbrella: Animation
    val bear_mask: Animation
    val scarh_high: Animation
    val scarh_low: Animation
    val pet: Animation

    // 누운
    val snow1: Animation
    val snow1_1: Animation
    val snow2: Animation
    val snow2_2: Animation

    // 비이
    val rain1: Animation
    val rain2: Animation
    val rain3: Animation

    //구르미
    val cloud1: Animation
    val cloud2: Animation
    val cloud3: Animation

    init {
        // 애니메이션 할당
        val appContext = WeatherBearApp.appContext
        bear_ear_r = AnimationUtils.loadAnimation(appContext, R.anim.bear_ear_right)
        bear_ear_l = AnimationUtils.loadAnimation(appContext, R.anim.bear_ear_left)
        leg_frong = AnimationUtils.loadAnimation(appContext, R.anim.leg_frong)
        leg_frong_good = AnimationUtils.loadAnimation(appContext, R.anim.leg_frong_good)
        leg_frong_bad = AnimationUtils.loadAnimation(appContext, R.anim.leg_frong_bad)
        umbrella = AnimationUtils.loadAnimation(appContext, R.anim.umbrella)
        bear_mask = AnimationUtils.loadAnimation(appContext, R.anim.bear_mask)
        scarh_high = AnimationUtils.loadAnimation(appContext, R.anim.scarf_high)
        scarh_low = AnimationUtils.loadAnimation(appContext, R.anim.scarf_low)
        pet = AnimationUtils.loadAnimation(appContext, R.anim.pet)

        snow1 = AnimationUtils.loadAnimation(appContext, R.anim.bg_snow_1)
        snow1_1 = AnimationUtils.loadAnimation(appContext, R.anim.bg_snow_1_2)
        snow2 = AnimationUtils.loadAnimation(appContext, R.anim.bg_snow_2)
        snow2_2 = AnimationUtils.loadAnimation(appContext, R.anim.bg_snow_2_2)

        rain1 = AnimationUtils.loadAnimation(appContext, R.anim.bg_rainy_1)
        rain2 = AnimationUtils.loadAnimation(appContext, R.anim.bg_rainy_2)
        rain3 = AnimationUtils.loadAnimation(appContext, R.anim.bg_rainy_3)

        cloud1 = AnimationUtils.loadAnimation(appContext, R.anim.bg_cloud_1)
        cloud2 = AnimationUtils.loadAnimation(appContext, R.anim.bg_cloud_2)
        cloud3 = AnimationUtils.loadAnimation(appContext, R.anim.bg_cloud_3)

        snow_bear = AnimationUtils.loadAnimation(appContext, R.anim.snow_bear)
    }

    private fun View.startAnimationIfVisible(anim : Animation) {
        if(this.visibility == View.VISIBLE)
            this.startAnimation(anim)
        else
            this.clearAnimation()
    }

    fun updateAnimation(topBearViewGroup: ViewGroup) {
        //snowman
        topBearViewGroup.snowBear?.startAnimationIfVisible(snow_bear)
        //양 귀 흔들기
        topBearViewGroup.bear_ear_right?.startAnimationIfVisible(bear_ear_r)
        topBearViewGroup.bear_ear_left?.startAnimationIfVisible(bear_ear_l)
        // 양 팔 흔들기
        topBearViewGroup.bear_leg?.startAnimationIfVisible(leg_frong)
        topBearViewGroup.bear_leg_front_good?.startAnimationIfVisible(leg_frong_good)
        //스카프 흔들리기
        topBearViewGroup.bear_scarf_high?.startAnimationIfVisible(scarh_high)
        topBearViewGroup.bear_scarf_low?.startAnimationIfVisible(scarh_low)
        //snowman 스카프 흔들리기
        topBearViewGroup.snowBearScarfHigh?.startAnimationIfVisible(scarh_high)
        topBearViewGroup.snowBearScarfLow?.startAnimationIfVisible(scarh_low)
        // 비올 때 우산과 팔 동시움직임
        topBearViewGroup.bear_umbrella?.startAnimationIfVisible(umbrella)
        //미세먼지 농도 높을 때 마스크와 마스크 발 동시.
        topBearViewGroup.bear_leg_front_bad?.startAnimationIfVisible(leg_frong_bad)
        topBearViewGroup.handkerchief?.startAnimationIfVisible(bear_mask)
        //먼지 펫 움직이기
        topBearViewGroup.bear_pet?.startAnimationIfVisible(pet)
        topBearViewGroup.bear_pet_small?.startAnimationIfVisible(pet)
        topBearViewGroup.bear_pet_w?.startAnimationIfVisible(pet)

        //눈내림
        topBearViewGroup.ivSnow1?.startAnimationIfVisible(snow1)
        topBearViewGroup.ivSnow1_2?.startAnimationIfVisible(snow1_1)
        topBearViewGroup.ivSnow2?.startAnimationIfVisible(snow2)
        topBearViewGroup.ivSnow2_2?.startAnimationIfVisible(snow2_2)

        // 비내림
        topBearViewGroup.ivRain1?.startAnimationIfVisible(rain1)
        topBearViewGroup.ivRain2?.startAnimationIfVisible(rain2)
        topBearViewGroup.ivRain2_3?.startAnimationIfVisible(rain3)

        // 구름
        topBearViewGroup.ivCloud1?.startAnimationIfVisible(cloud1)
        topBearViewGroup.ivCloud1_2?.startAnimationIfVisible(cloud1)
        topBearViewGroup.ivCloud2?.startAnimationIfVisible(cloud2)
        topBearViewGroup.ivCloud2_2?.startAnimationIfVisible(cloud2)
        topBearViewGroup.ivCloud3?.startAnimationIfVisible(cloud3)
    }
}