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
class WeatherBoxWidget : WeatherBearWidgetCommon() {

    override fun updateAppWidget(weatherData : WidgetWeatherData, context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {

        //홈스크린의 위젯 xml에 대한 인스턴스를 받아온다,
        val remoteViews = RemoteViews(context.packageName, R.layout.weather_box_widget)
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
        val weather = weatherData.weather
        //좋음,보통,나쁨.매우나쁨
        val fineDustLevel = weatherData.fineDustLevel
        val locationText = weatherData.locationText
        val temperature = weatherData.temperature.toString()

        var bearSkin = R.drawable.ic_bear_head_01
        var bearSnow = R.drawable.ic_msg_bear_head_snow_good
        var bearFace: Int
        //낮:true 밤:false
        var time = true
        val date = Date().hours
        time = date in 6..17

        bearSnow = bearHeadFace(fineDustLevel, weather, time)

        bearSkin = when (fineDustLevel) {
            WdgFineDustLvl.GOOD -> (R.drawable.ic_bear_head_01)
            WdgFineDustLvl.NORMAL -> (R.drawable.ic_bear_head_02)
            WdgFineDustLvl.BAD -> (R.drawable.ic_bear_head_03)
            WdgFineDustLvl.WORST -> (R.drawable.ic_bear_head_04)
            else -> (R.drawable.ic_bear_head_01) //unknown
        }

        if (time) {
            bearFace = R.drawable.ic_msg_bear_face_good
            if (fineDustLevel == WdgFineDustLvl.BAD || fineDustLevel == WdgFineDustLvl.WORST)
                bearFace = R.drawable.ic_msg_bear_face_bad
        } else {
            bearFace = R.drawable.ic_msg_bear_face_sleep
        }


        val weatherBox = RemoteViews(context.packageName, R.layout.weather_box_widget)

        weatherBox.setImageViewResource(R.id.widgetBearHeadBase, bearSkin)
        weatherBox.setImageViewResource(R.id.widgetBearFace, bearFace)
        weatherBox.setImageViewResource(R.id.widgetBearFaceSnow, bearSnow)

        val weatherText = when(weatherData.weather) {
            WdgWeather.SUNNY -> context.getString(R.string.msg_sunny)
            WdgWeather.SNOW -> context.getString(R.string.msg_snow)
            WdgWeather.RAIN -> context.getString(R.string.msg_rain)
            WdgWeather.CLOUDY -> context.getString(R.string.msg_cloud)
            WdgWeather.THUNDER -> context.getString(R.string.msg_thunder)
            WdgWeather.NO_DATA -> context.getString(R.string.noData)
        }
        val fineDustLevelText = when(weatherData.fineDustLevel) {
            WdgFineDustLvl.GOOD -> context.getString(R.string.fine_dust_good)
            WdgFineDustLvl.NORMAL -> context.getString(R.string.fine_dust_normal)
            WdgFineDustLvl.BAD -> context.getString(R.string.fine_dust_bad)
            WdgFineDustLvl.WORST -> context.getString(R.string.fine_dust_very_bad)
            WdgFineDustLvl.NO_DATA -> context.getString(R.string.noData)
        }

        weatherBox.setTextViewText(R.id.tvWidgetFineDust, fineDustLevelText)
        weatherBox.setTextViewText(R.id.tvWidgetLocation, locationText)
        weatherBox.setTextViewText(R.id.tvWidgetTemperature, temperature)
        weatherBox.setTextViewText(R.id.tvWidgetWeather, weatherText)

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, weatherBox)
    }

    private fun bearHeadFace(fineDustLevel: WdgFineDustLvl, weather: WdgWeather, time: Boolean): Int {
        if (time) {
            if (weather == WdgWeather.SNOW) {
                return if (fineDustLevel == WdgFineDustLvl.GOOD) {
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

