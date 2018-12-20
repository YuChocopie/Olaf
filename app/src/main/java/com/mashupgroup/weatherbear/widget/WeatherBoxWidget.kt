package com.mashupgroup.weatherbear.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews

import com.mashupgroup.weatherbear.R

/**
 * Implementation of App Widget functionality.
 */
class WeatherBoxWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
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
                                     appWidgetId: Int) {

            //SNOW, RAINY, THUNDER_RAINY, WIND, CLOUD, SUNNY, HEAVY_SNOW
            var weather = "SNOW"
            //낮:true 밤:false
            var time = true
            //좋음,보통,나쁨.매우나쁨
            var fineDustLevel = "매우나쁨"
            var locationText = "서울시 중구"
            var temperature = "-13"
            var bearSkin = R.drawable.ic_bear_head_01
            var bearSnow = R.drawable.ic_msg_bear_head_snow_good
            var bearFace: Int

            bearSnow = bearHeadFace(fineDustLevel, weather, time)

            when(fineDustLevel){
                "좋음" -> bearSkin=(R.drawable.ic_bear_head_01)
                "보통" -> bearSkin=(R.drawable.ic_bear_head_02)
                "나쁨" -> bearSkin=(R.drawable.ic_bear_head_03)
                "매우나쁨" -> bearSkin=(R.drawable.ic_bear_head_04)
            }

            if(time) {
                bearFace=R.drawable.ic_msg_bear_face_good
                if(fineDustLevel=="나쁨" || fineDustLevel == "매우나쁨")
                    bearFace=R.drawable.ic_msg_bear_face_bad
            }else{
                bearFace=R.drawable.ic_msg_bear_face_sleep
            }


            val weatherBox = RemoteViews(context.packageName, R.layout.weather_box_widget)

            weatherBox.setImageViewResource(R.id.widgetBearHeadBase, bearSkin)
            weatherBox.setImageViewResource(R.id.widgetBearFace, bearFace)
            weatherBox.setImageViewResource(R.id.widgetBearFaceSnow, bearSnow)

            weatherBox.setTextViewText(R.id.tvWidgetFineDust, fineDustLevel)
            weatherBox.setTextViewText(R.id.tvWidgetLocation, locationText)
            weatherBox.setTextViewText(R.id.tvWidgetTemperature, temperature)
            weatherBox.setTextViewText(R.id.tvWidgetWeather, weather)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, weatherBox)
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

