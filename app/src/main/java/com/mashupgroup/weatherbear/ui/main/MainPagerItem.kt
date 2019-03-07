package com.mashupgroup.weatherbear.ui.main

import android.location.Address
import com.mashupgroup.weatherbear.ui.main.viewmodel.BackgroundViewModel
import com.mashupgroup.weatherbear.ui.main.viewmodel.BearViewModel
import com.mashupgroup.weatherbear.ui.main.viewmodel.IsDayViewModel

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