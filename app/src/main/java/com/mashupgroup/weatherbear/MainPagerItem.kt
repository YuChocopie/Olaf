package com.mashupgroup.weatherbear

import android.location.Address

class MainPagerItem(_vmBear:    BearViewModel,
                    _vmBG:      BackgroundViewModel,
                    _vmInfo:    IsDayViewModel,
                    _address:   Address) {
    val vmBear : BearViewModel = _vmBear
    val vmBG : BackgroundViewModel = _vmBG
    val vmInfo : IsDayViewModel = _vmInfo
    val address : Address = _address
}