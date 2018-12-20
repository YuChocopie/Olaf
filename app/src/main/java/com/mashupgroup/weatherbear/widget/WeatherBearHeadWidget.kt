package com.mashupgroup.weatherbear.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews

import com.mashupgroup.weatherbear.R

/**
 * Implementation of App Widget functionality.
 */
class WeatherBearHeadWidget : AppWidgetProvider() {

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
            //낮:true 밤:false
            //미세먼지 : 좋음,보통,나쁨.매우나쁨
            var weather = "HEAVY_SNOW"
            var time = true
            var fineDustLevel = "좋음"

            var bearSkin = R.drawable.ic_bear_head_01
            var bearSnow: Int
            var bearFace: Int

            when(fineDustLevel){
                "좋음" -> bearSkin=(R.drawable.ic_bear_head_01)
                "보통" -> bearSkin=(R.drawable.ic_bear_head_02)
                "나쁨" -> bearSkin=(R.drawable.ic_bear_head_03)
                "매우나쁨" -> bearSkin=(R.drawable.ic_bear_head_04)
            }

            if (time) {
                bearFace=R.drawable.ic_msg_bear_face_good
                if(fineDustLevel=="나쁨" || fineDustLevel == "매우나쁨") {
                    bearFace=R.drawable.ic_msg_bear_face_bad
                }
                if (weather == "SNOW" || weather == "HEAVY_SNOW") {
                    bearSnow=(R.drawable.ic_msg_bear_head_snow)
                    if (fineDustLevel == "좋음") {
                        bearSnow=(R.drawable.ic_msg_bear_head_snow_good)
                } }else
                    bearSnow=(R.drawable.ic_msg_bear_head_snow_none)
            } else {
                bearFace=R.drawable.ic_msg_bear_face_sleep
                bearSnow = (R.drawable.ic_msg_bear_head_snow_none)
            }

            // Construct the RemoteViews object
            val BearHeadview = RemoteViews(context.packageName, R.layout.weather_bear_head_widget)

            BearHeadview.setImageViewResource(R.id.widgetBearHeadBase, bearSkin)
            BearHeadview.setImageViewResource(R.id.widgetBearFace, bearFace)
            BearHeadview.setImageViewResource(R.id.widgetBearFaceSnow, bearSnow)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, BearHeadview)
        }
    }
}

