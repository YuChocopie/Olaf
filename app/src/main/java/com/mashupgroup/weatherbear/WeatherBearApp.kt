package com.mashupgroup.weatherbear

import android.app.Application
import android.content.Context

class WeatherBearApp : Application() {
    override fun onCreate() {
        super.onCreate()
        application = this
    }

    companion object {
        @JvmStatic
        lateinit var application: Application
            private set

        val appContext: Context
            get() = application.applicationContext
    }
}