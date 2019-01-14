package com.mashupgroup.weatherbear

import android.location.Address
import com.mashupgroup.weatherbear.viewmodels.BackgroundViewModel
import com.mashupgroup.weatherbear.viewmodels.BearViewModel
import com.mashupgroup.weatherbear.viewmodels.IsDayViewModel

class MainPagerItem(_vmBear: BearViewModel,
                    _vmBG: BackgroundViewModel,
                    _vmInfo: IsDayViewModel,
                    _address:   Address,
                    _graphArray: IntArray) {
    val vmBear : BearViewModel = _vmBear
    val vmBG : BackgroundViewModel = _vmBG
    val vmInfo : IsDayViewModel = _vmInfo
    val address : Address = _address
    var graphArray : IntArray = _graphArray
}