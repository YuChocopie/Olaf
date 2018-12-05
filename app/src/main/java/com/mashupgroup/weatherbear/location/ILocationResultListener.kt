package com.mashupgroup.weatherbear.location

import android.location.Address
import android.location.Location

interface ILocationResultListener {
    fun onLocationReady(location: Location?, address: Address?)
}