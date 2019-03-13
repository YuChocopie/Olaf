package com.mashupgroup.weatherbear.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.mashupgroup.weatherbear.ui.main.MainActivity

import com.mashupgroup.weatherbear.R
import java.util.*

/**
 * Implementation of App Widget functionality.
 */
class WeatherBearHeadWidgetBg : WeatherBearWidgetCommon() {

    override fun updateAppWidget(weatherData : WidgetWeatherData, context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {


        //SNOW, RAINY, THUNDER_RAINY, WIND, CLOUD, SUNNY, HEAVY_SNOW
        //미세먼지 : 좋음,보통,나쁨.매우나쁨
        //낮:true 밤:false
        val weather = weatherData.weather
        val fineDustLevel = weatherData.fineDustLevel
        var time = true
        val date = Date().hours
        time = date in 6..17

        var bearSkin = R.drawable.ic_bear_head_01
        var bearSnow: Int
        var bearFace: Int

        //홈스크린의 위젯 xml에 대한 인스턴스를 받아온다,
        val remoteViews = RemoteViews(context.packageName, R.layout.weather_bear_head_widget_bg)
        //main activity와 위젯간 데이터 교환의 매개채
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        remoteViews.setOnClickPendingIntent(R.id.widgetBearHead, pendingIntent)
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews)

        bearSkin = when (fineDustLevel) {
            WdgFineDustLvl.GOOD -> (R.drawable.ic_bear_head_01)
            WdgFineDustLvl.NORMAL -> (R.drawable.ic_bear_head_02)
            WdgFineDustLvl.BAD -> (R.drawable.ic_bear_head_03)
            WdgFineDustLvl.WORST -> (R.drawable.ic_bear_head_04)
            else -> (R.drawable.ic_bear_head_01) //unknown
        }

        if (time) {
            bearFace = R.drawable.ic_msg_bear_face_good
            if (fineDustLevel == WdgFineDustLvl.BAD || fineDustLevel == WdgFineDustLvl.WORST) {
                bearFace = R.drawable.ic_msg_bear_face_bad
            }
            if (weather == WdgWeather.SNOW) {
                bearSnow = (R.drawable.ic_msg_bear_head_snow)
                if (fineDustLevel == WdgFineDustLvl.GOOD) {
                    bearSnow = (R.drawable.ic_msg_bear_head_snow_good)
                }
            } else
                bearSnow = (R.drawable.ic_msg_bear_head_snow_none)
        } else {
            bearFace = R.drawable.ic_msg_bear_face_sleep
            bearSnow = (R.drawable.ic_msg_bear_head_snow_none)
        }

        // Construct the RemoteViews object
        val BearHeadviewBg = RemoteViews(context.packageName, R.layout
        .weather_bear_head_widget_bg)

        BearHeadviewBg.setImageViewResource(R.id.widgetBearHeadBase, bearSkin)
        BearHeadviewBg.setImageViewResource(R.id.widgetBearFace, bearFace)
        BearHeadviewBg.setImageViewResource(R.id.widgetBearFaceSnow, bearSnow)

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, BearHeadviewBg)
    }
}

