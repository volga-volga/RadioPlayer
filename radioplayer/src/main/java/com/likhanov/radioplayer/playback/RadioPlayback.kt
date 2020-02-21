package com.likhanov.radioplayer.playback

import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log

class RadioPlayback(url: String) : Playback {

    private var state = PlaybackStateCompat.STATE_NONE
    private var playOnFocusGain: Boolean = false
    private var callback: Playback.Callback? = null

    private val playerCallback: RadioPlayerCallback = object : RadioPlayerCallback() {

        override fun onDaastError() {
            callback?.onDaastError()
        }

        override fun onMeta(p0: String?) {
        }

        override fun onDaastSkip() {
        }

        override fun onPlay() {
            callback?.onPlaybackStatusChanged(PlaybackStateCompat.STATE_PLAYING)
        }

        override fun onPause() {
            callback?.onPlaybackStatusChanged(PlaybackStateCompat.STATE_PAUSED)
        }

        override fun onDaastStart(p0: String?, p1: String?, p2: String?) {
            callback?.onDaastStart(p1, p2)
        }

        override fun onDaastEnd() {
            callback?.onDaastEnd()
        }

        override fun onError(p0: String?, p1: Int) {
            callback?.onError("Player error $p0, $p1")
            releaseResources()
        }
    }
    private var player = Player(url, playerCallback)

    override fun start() {
    }

    override fun stop(notifyListeners: Boolean) {
        releaseResources()
    }

    override fun setState(state: Int) {
        this.state = state
    }

    override fun getState(): Int {
        return state
    }

    override fun isConnected(): Boolean = true

    override fun isPlaying(): Boolean = playOnFocusGain

    override fun getCurrentStreamPosition(): Long = 0

    override fun updateLastKnownStreamPosition() {
    }

    override fun play() {
        player.play()
        playOnFocusGain = true
    }

    override fun pause() {
        player.pause()
        playOnFocusGain = false
    }

    fun updateUrl(url: String, masterStream: Boolean) = player.updateUrl(url, masterStream)

    fun playing() = player.isPlaying()

    fun isPaused() = player.isPaused()

    fun isRestarted() = player.isRestarted()

    fun setAd(url: String) = player.setAd(url)

    fun daastClicked() = player.daastClicked()

    override fun seekTo(position: Long) {

    }

    override fun setVolume(volume: Float) {
    }

    override fun getVolume(): Float {
        return 0f
    }

    override fun setCurrentMediaId(mediaId: String?) {
    }

    override fun getCurrentMediaId(): String = ""

    override fun setCallback(callback: Playback.Callback?) {
        this.callback = callback
    }

    private fun releaseResources() {
        player.release()
    }
}