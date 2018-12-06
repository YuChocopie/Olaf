package com.mashupgroup.weatherbear

import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils


class BearModel {
    /*
        미세먼지 단계 4단계
            - 곰의 색
            - 눈의 색
            - 앞발의 변경(1,2단계:leg_frong_good 3,4단계: leg_frong_bad + 마스크)
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

/*    미세먼지 : 좋음, 보통, 나쁨, 매우나쁨을 1,2,3,4(int)
      날씨 : 눈(snow), 비(rainy), 천둥번개(thunder_rainy), 바람(wind), 구름(cloud),맑음(sunny), 폭설(heavy_snow)
      시간 : 낮="true" 밤="false"       */

    private var fineDust: Int = 1
    private var weather: String = "snow"
    private var time: Boolean = true

    var resource = WeatherBearApp.appContext.resources
    var bearSkinColor = resource.getColor(R.color.bearBad)
    var bearSnowColor = resource.getColor(R.color.bearSnowbad)
    var bearFaceImage = resource.getDrawable(R.drawable.ic_msg_bear_face_bad)
    var bearSnowVisibilty = true
    var bearLegVisibilty = true
    var bearPetVisibilty = true
    var bearSunglesesVisibilty = false
    var bearumbrellaVisibilty = true

    //폭설일 경우 눈사람으로 변경
    private fun snowMan() {
        //TODO: change snowman... include && animation 혹은 모든 visibility 를 gone 으로 바꾸고 눈사람 추가
        if (weather == "heavySnow") {
        } else {
            setBear()
        }
    }

    private fun setBear() {
        bearSkin()
        bearSnow()
        bearLeg()
        bearFace()
        bearPet()
        bearSunglesses()
    }

    fun bearSkin() {
        when (fineDust) {
            1 -> bearSkinColor = resource.getColor(R.color.bearGood)
            2 -> bearSkinColor = resource.getColor(R.color.bearNomal)
            3 -> bearSkinColor = resource.getColor(R.color.bearBad)
            4 -> bearSkinColor = resource.getColor(R.color.bearVeryBad)
        }
    }

    private fun bearSnow() {
        if (weather == "heavy_snow" || weather == "snow") {
            bearSnowVisibilty = true
            when (fineDust) {
                1 -> bearSnowColor = resource.getColor((R.color.bearSnowGood))
                2, 3, 4 -> bearSnowColor = resource.getColor((R.color.bearSnowbad))
            }
        } else {
            bearSnowVisibilty = false
        }
    }

    private fun bearFace() {
        if (time) {
            when (fineDust) {
                1, 2 -> bearFaceImage = resource.getDrawable(R.drawable.ic_msg_bear_face_good)
                3, 4 -> bearFaceImage = resource.getDrawable(R.drawable.ic_msg_bear_face_bad)
            }

        } else {
            bearFaceImage = resource.getDrawable(R.drawable.ic_msg_bear_face_sleep)
        }
    }

    private fun bearLeg() {
        if (weather == "rainy" || weather == "thunder_rainy") {
            //TODO: 우산 애니매이션 시작, 비옴
            bearumbrellaVisibilty = true
            bearLegVisibilty = true
        } else {
            if (time) {
                if (fineDust >= 3) {
                    bearLegVisibilty = false
                }
            } else {
                bearLegVisibilty = true
            }
        }
    }

    private fun bearPet() {
        bearPetVisibilty = fineDust == 4

    }

    private fun bearSunglesses() {
        if (time) {
            if (weather == "wind" || weather == "cloud" || weather == "sunny") {
                bearSunglesesVisibilty = true
            }

        } else {
            bearSunglesesVisibilty = false
        }

    }

}
