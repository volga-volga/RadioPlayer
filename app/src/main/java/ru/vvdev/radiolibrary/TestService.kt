package ru.vvdev.radiolibrary

import com.likhanov.radioplayer.radio.RadioService

class TestService : RadioService() {

    override fun onCreate() {
        super.onCreate()
        init(this, TestService::class.java)
        updateUrl("http://icecast-studio21.cdnvideo.ru/S21_1")
        setSessionActivity(MainActivity::class.java)
        setActivityForNotificationIntent(MainActivity::class.java)
    }
}