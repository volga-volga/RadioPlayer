package com.likhanov.radioplayer.playback

import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import com.infteh.startrekplayer.StartrekAndroid
import com.infteh.startrekplayer.StartrekPlayer


class Player(private var url: String = "", private val listener: RadioPlayerCallback) {
    private lateinit var m_playbackBuilder: PlaybackStateCompat.Builder
    private lateinit var mPlayback: StartrekPlayer
    private var masterStream = false
    private var lastPlayedUrl = ""

    init {
        System.loadLibrary("StartrekPlayerNative" + StartrekAndroid.PREFERRED_ABI)
        init()
    }

    private fun init() {
        // STPlayer
        mPlayback = StartrekPlayer.create()
        mPlayback.setDelegate(listener)

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

    fun daastClicked() = mPlayback.daastClick()

    fun updateUrl(url: String, masterStream: Boolean = false) {
        this.url = url
        this.masterStream = masterStream
    }

    fun isPlaying() = mPlayback.isPlaying

    fun isPaused() = mPlayback.isPaused

    fun isRestarted() = mPlayback.isRestarted

    fun play() {
        try {
            when {
                lastPlayedUrl != url -> mPlayback.playUrl(url)
                else -> mPlayback.play()
            }
            lastPlayedUrl = url
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


    fun release() {
        mPlayback.stop()
    }
}