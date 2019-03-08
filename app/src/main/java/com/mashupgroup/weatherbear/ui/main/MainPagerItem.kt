package com.mashupgroup.weatherbear.ui.main

import android.location.Address
import com.mashupgroup.weatherbear.ui.main.viewmodel.BackgroundViewModel
import com.mashupgroup.weatherbear.ui.main.viewmodel.BearViewModel
import com.mashupgroup.weatherbear.ui.main.viewmodel.IsDayViewModel

data class MainPagerItem(
        var vmBear: BearViewModel,
        var vmBG: BackgroundViewModel,
        var vmInfo: IsDayViewModel,
        var address:   Address,
        var graphArray: IntArray
)