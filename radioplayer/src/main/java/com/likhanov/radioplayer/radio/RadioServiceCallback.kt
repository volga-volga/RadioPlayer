package com.likhanov.radioplayer.radio

import android.support.v4.media.MediaBrowserServiceCompat
import com.likhanov.radioplayer.model.NotificationData

interface RadioServiceCallback {
    fun init(service: MediaBrowserServiceCompat, serviceClass: Class<*>)
    fun updateUrl(url: String, masterStream: Boolean = false)
    fun setAd(url:String)
    fun setSessionActivity(activity: Class<*>)
    fun updateNotification(data: NotificationData?)
    fun setDefaultDrawable(drawableRes: Int)
    fun setActivityForNotificationIntent(activity: Class<*>)
    fun setNotificationDrawable(drawableRes: Int)
}