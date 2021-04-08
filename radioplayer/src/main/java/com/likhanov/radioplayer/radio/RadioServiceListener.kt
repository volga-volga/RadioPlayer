package com.likhanov.radioplayer.radio

import androidx.media.MediaBrowserServiceCompat
import com.likhanov.radioplayer.model.NotificationData

interface RadioServiceListener {
    fun init(service: MediaBrowserServiceCompat, serviceClass: Class<*>)
    fun updateUrl(url: String, masterStream: Boolean = false)
    fun setAd(url:String)
    fun setSessionActivity(activity: Class<*>)
    fun updateNotification(data: NotificationData?)
    fun setDefaultDrawable(drawableRes: Int)
    fun setActivityForNotificationIntent(activity: Class<*>)
    fun setNotificationDrawable(drawableRes: Int)
    fun isPlaying(): Boolean
    fun isPaused(): Boolean
    fun isRestarted(): Boolean
    fun setCallback(callback: RadioServiceCallback)
    fun daastClicked()
}