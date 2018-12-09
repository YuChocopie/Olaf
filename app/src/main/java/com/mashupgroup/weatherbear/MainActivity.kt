package com.mashupgroup.weatherbear

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bear.*

class MainActivity : AppCompatActivity() {
    var mainPagerAdapter : MainPagerAdapter = MainPagerAdapter(this);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bear_ear_r: Animation = AnimationUtils.loadAnimation(this, R.anim.bear_ear_right)
        val bear_ear_l: Animation = AnimationUtils.loadAnimation(this, R.anim.bear_ear_left)
        val leg_frong: Animation = AnimationUtils.loadAnimation(this, R.anim.leg_frong)
        val leg_frong_good: Animation = AnimationUtils.loadAnimation(this, R.anim.leg_frong_good)
        val leg_frong_bed: Animation = AnimationUtils.loadAnimation(this, R.anim.leg_frong_bed)
        val umbrella: Animation = AnimationUtils.loadAnimation(this, R.anim.umbrella)
        val bear_mask: Animation = AnimationUtils.loadAnimation(this, R.anim.bear_mask)
        val scarh_high: Animation = AnimationUtils.loadAnimation(this, R.anim.scarf_high)
        val scarh_low: Animation = AnimationUtils.loadAnimation(this, R.anim.scarf_low)
        val pet: Animation = AnimationUtils.loadAnimation(this, R.anim.pet)
        //양 귀 흔들기
        bear_ear_right.startAnimation(bear_ear_r)
        bear_ear_left.startAnimation(bear_ear_l)
        // 양 팔 흔들기
        bear_leg.startAnimation(leg_frong)
        bear_leg_front_good.startAnimation(leg_frong_good)
        //스카프 흔들리기
        bear_scarf_high.startAnimation(scarh_high)
        bear_scarf_low.startAnimation(scarh_low)
        // 비올 때 우산과 팔 동시움직임
        bear_umbrella.startAnimation(umbrella)
        //미세먼지 농도 높을 때 마스크와 마스크 발 동시.
        bear_leg_front_bed.startAnimation(leg_frong_bed)
        handkerchief.startAnimation(bear_mask)
        //먼지 펫 움직이기
        bear_pet.startAnimation(pet)
        bear_pet_small.startAnimation(pet)
        bear_pet_w.startAnimation(pet)

        /*
        미세먼지 단계 4단계
            - 곰의 색
            - 눈의 색
            - 앞발의 변경(1,2단계:leg_frong_good 3,4단계: leg_frong_bed + 마스크)
            - 표정의 변경(1,2   : good            3,4 : bad)
            - 옆의 펫의 여부(4단계에만 나타남)

        날씨에 따른 변화
            - 비 : 곰 손에 우산 추가 ++ 미세먼지 앞발 변하지 않음. leg_frong_good 로 유지
                   배경에 비 내림
            - 눈 : 배경에 눈 내림
            - 폭설 : 배경에 눈 내림 + 눈사람으로 변경
            - 구름, 바람 : 배경에 구름 지나감
            - 맑음 : 그냥 맑음

        시간
            - 밤/낮 : 곰의 얼굴 변경 //밤에 무조건 잠. */

        // ViewPager 초기화
        viewPager.initialize(mainIndicator)
        viewPager.adapter = mainPagerAdapter

        viewPager.initIndicator()
    }
}

