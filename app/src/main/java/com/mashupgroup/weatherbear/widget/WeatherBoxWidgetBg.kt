package com.mashupgroup.weatherbear.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.mashupgroup.weatherbear.MainActivity

import com.mashupgroup.weatherbear.R
import java.util.*

/**
 * Implementation of App Widget functionality.
 */
class WeatherBoxWidgetBg : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        var weather = "SNOW"
        //좋음,보통,나쁨.매우나쁨
        var fineDustLevel = "나쁨"
        var locationText = "서울시 강남  구"
        var temperature = -12


        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId,weather, fineDustLevel,
                    locationText, temperature)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {

        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager,
                                     appWidgetId: Int, nWeather: String, nFineDustLevel: String,
                                     nLocationText: String, nTemperature: Int) {

            //홈스크린의 위젯 xml에 대한 인스턴스를 받아온다,
            val remoteViews = RemoteViews(context.packageName, R.layout.weather_box_widget_bg)
            //main activity와 위젯간 데이터 교환의 매개채
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context,0,intent,0)
            remoteViews.setOnClickPendingIntent(R.id.widgetBearFace,pendingIntent)
            remoteViews.setOnClickPendingIntent(R.id.tvWidgetLocation,pendingIntent)
            remoteViews.setOnClickPendingIntent(R.id.tvWidgetTemperature
                    ,pendingIntent)
            remoteViews.setOnClickPendingIntent(R.id.tvWidgetFineDust,pendingIntent)
            remoteViews.setOnClickPendingIntent(R.id.tvWidgetWeather,pendingIntent)
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)


            //SNOW, RAINY, THUNDER_RAINY, WIND, CLOUD, SUNNY, HEAVY_SNOW
            val weather = nWeather
            //좋음,보통,나쁨.매우나쁨
            val fineDustLevel = nFineDustLevel
            val locationText = nLocationText
            val temperature = nTemperature.toString()

            var bearSkin = R.drawable.ic_bear_head_01
            var bearSnow = R.drawable.ic_msg_bear_head_snow_good
            var bearFace: Int
            //낮:true 밤:false
            var time = true
            val date = Date().hours
            time = date in 6..17

            bearSnow = bearHeadFace(fineDustLevel, weather, time)

            when (fineDustLevel) {
                "좋음" -> bearSkin = (R.drawable.ic_bear_head_01)
                "보통" -> bearSkin = (R.drawable.ic_bear_head_02)
                "나쁨" -> bearSkin = (R.drawable.ic_bear_head_03)
                "매우나쁨" -> bearSkin = (R.drawable.ic_bear_head_04)
            }

            if (time) {
                bearFace = R.drawable.ic_msg_bear_face_good
                if (fineDustLevel == "나쁨" || fineDustLevel == "매우나쁨")
                    bearFace = R.drawable.ic_msg_bear_face_bad
            } else {
                bearFace = R.drawable.ic_msg_bear_face_sleep
            }


            val weatherBoxBg = RemoteViews(context.packageName, R.layout.weather_box_widget_bg)

            weatherBoxBg.setImageViewResource(R.id.widgetBearHeadBase, bearSkin)
            weatherBoxBg.setImageViewResource(R.id.widgetBearFace, bearFace)
            weatherBoxBg.setImageViewResource(R.id.widgetBearFaceSnow, bearSnow)

            weatherBoxBg.setTextViewText(R.id.tvWidgetFineDust, fineDustLevel)
            weatherBoxBg.setTextViewText(R.id.tvWidgetLocation, locationText)
            weatherBoxBg.setTextViewText(R.id.tvWidgetTemperature, temperature)
            weatherBoxBg.setTextViewText(R.id.tvWidgetWeather, weather)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, weatherBoxBg)
        }

        private fun bearHeadFace(fineDustLevel: String, weather: String, time: Boolean): Int {
            if (time) {
                if (weather == "SNOW" || weather == "HEAVY_SNOW") {
                    return if (fineDustLevel == "좋음") {
                        (R.drawable.ic_msg_bear_head_snow_good)
                    } else
                        (R.drawable.ic_msg_bear_head_snow)
                }
                return (R.drawable.ic_msg_bear_head_snow_none)
            } else {
                return (R.drawable.ic_msg_bear_head_snow_none)
            }

        }
    }
}

