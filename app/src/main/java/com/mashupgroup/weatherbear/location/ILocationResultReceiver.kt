package com.mashupgroup.weatherbear.location

import android.location.Address

interface ILocationResultReceiver {
    fun onLocationReady(addressList: List<Address>?)
}