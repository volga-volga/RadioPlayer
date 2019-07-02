package com.likhanov.radioplayer.radio

import com.likhanov.radioplayer.model.NotificationData

interface RadioServiceCallback {
    fun updateUrl(url: String)
    fun setAd(url:String)
    fun setSessionActivity(activity: Class<*>)
    fun updateNotification(data: NotificationData?)
}