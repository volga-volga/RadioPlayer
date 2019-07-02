package com.likhanov.radioplayer

import android.app.Application
import com.likhanov.radioplayer.util.Store

class RadioApp : Application(){

    override fun onCreate() {
        super.onCreate()
        Store.init(applicationContext)
    }
}