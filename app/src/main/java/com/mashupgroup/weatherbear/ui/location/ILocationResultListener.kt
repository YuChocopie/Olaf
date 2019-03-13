package com.mashupgroup.weatherbear.ui.location

import android.location.Address
import android.location.Location

interface ILocationResultListener {
    fun onLocationReady(location: Location?, address: Address?)
}