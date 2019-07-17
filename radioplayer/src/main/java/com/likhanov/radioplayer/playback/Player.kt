package com.likhanov.radioplayer.playback

import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import com.likhanov.radioplayer.util.Store
import org.radiobox.startrek_player.StartrekPlayer
import java.util.*


class Player(private var url: String = "", private val listener: RadioPlayerCallback) {
    private lateinit var m_playbackBuilder: PlaybackStateCompat.Builder
    private lateinit var mPlayback: StartrekPlayer
    private var masterStream = false

    init {
        System.loadLibrary("startrek_player")
        init()
    }

    private fun init() {
        // STPlayer
        mPlayback = StartrekPlayer.create()
        mPlayback.setDelegate(listener)
        mPlayback.setNetBuffer(25000)
        mPlayback.setNetPrebuf(Math.round(100 * (2000.0 / 25000.0)).toInt())
        mPlayback.setAgent("Android Startrek Player Radio Service Application")
        mPlayback.isRestarted = true
        mPlayback.setReferer("Android Startrek Player")
        var userId = Store.getString(Store.PREF_USER_ID)
        if (userId.isBlank()) {
            Store.storeData(Store.PREF_USER_ID, UUID.randomUUID().toString())
            userId = Store.getString(Store.PREF_USER_ID)
        }
        mPlayback.setUserId(userId)

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player
        m_playbackBuilder = PlaybackStateCompat.Builder()
        m_playbackBuilder.setActions(
            PlaybackStateCompat.ACTION_PLAY_FROM_URI
                    or PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
                    or PlaybackStateCompat.ACTION_PLAY
                    or PlaybackStateCompat.ACTION_STOP
                    or PlaybackStateCompat.ACTION_PAUSE
        )
    }

    fun setAd(adUrl: String) {
        mPlayback.setDaastUrl(adUrl)
    }

    fun updateUrl(url: String, masterStream: Boolean = false) {
        this.url = url
        this.masterStream = masterStream
    }

    fun isPlaying() = mPlayback.isPlaying

    fun isPaused() = mPlayback.isPaused

    fun isRestarted() = mPlayback.isRestarted

    fun play() {
        try {
            if (masterStream) mPlayback.playMasterUrl(url)
            else mPlayback.playUrl(url)
        } catch (err: Exception) {
            Log.e("stateTag", "play error", err)
        }
    }

    fun pause() {
        try {
            mPlayback.pause()
        } catch (err: Exception) {
            err.printStackTrace()
        }
    }
}