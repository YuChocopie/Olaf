package com.mashupgroup.weatherbear

import android.location.Address
import com.mashupgroup.weatherbear.viewmodel.BackgroundViewModel
import com.mashupgroup.weatherbear.viewmodel.BearViewModel
import com.mashupgroup.weatherbear.viewmodel.IsDayViewModel

class MainPagerItem(_vmBear: BearViewModel,
                    _vmBG: BackgroundViewModel,
                    _vmInfo: IsDayViewModel,
                    _address:   Address) {
    val vmBear : BearViewModel = _vmBear
    val vmBG : BackgroundViewModel = _vmBG
    val vmInfo : IsDayViewModel = _vmInfo
    val address : Address = _address
}