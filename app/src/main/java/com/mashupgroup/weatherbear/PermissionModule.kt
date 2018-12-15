package com.mashupgroup.weatherbear

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log

class PermissionModule {

    fun setupPermissions(context: Context, activity: Activity) {
        val permissions : List<String> = listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)

        for(permission in permissions) {
            val check = ContextCompat.checkSelfPermission(context,
                    permission)
            if(check != PackageManager.PERMISSION_GRANTED) {
                Log.i("setupPermissions", "Permission $permission denied")
                makeRequest(permission, activity)
            }
            Log.i("setupPermissions", "Permission $permission checked")
        }
    }

    private fun makeRequest(permission: String, activity: Activity) {
        ActivityCompat.requestPermissions(activity,
                arrayOf(permission),
                100)
    }

}