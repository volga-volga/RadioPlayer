package com.likhanov.radioplayer.radio

import android.support.v4.media.MediaBrowserServiceCompat
import com.likhanov.radioplayer.model.NotificationData

interface RadioServiceCallback {
    fun init(service: MediaBrowserServiceCompat)
    fun updateUrl(url: String)
    fun setAd(url:String)
    fun setSessionActivity(activity: Class<*>)
    fun updateNotification(data: NotificationData?)
}