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
    var vmBear : BearViewModel = _vmBear
    var vmBG : BackgroundViewModel = _vmBG
    var vmInfo : IsDayViewModel = _vmInfo
    var address : Address = _address
    var graphArray : IntArray = _graphArray
}