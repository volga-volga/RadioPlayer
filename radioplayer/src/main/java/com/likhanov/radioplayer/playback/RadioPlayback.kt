package com.likhanov.radioplayer.playback

import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log

class RadioPlayback(url: String) : Playback {

    private var state = PlaybackStateCompat.STATE_NONE
    private var playOnFocusGain: Boolean = false
    private var callback: Playback.Callback? = null

    private val playerCallback: RadioPlayerCallback = object : RadioPlayerCallback() {

        override fun daastError(message: String?) {
            callback?.onDaastError()
        }

        override fun isPlayingChanged(isPlaying: Boolean) {
            if(isPlaying) callback?.onPlaybackStatusChanged(PlaybackStateCompat.STATE_PLAYING)
            else callback?.onPlaybackStatusChanged(PlaybackStateCompat.STATE_PAUSED)
        }

        override fun daastStarted(meta: String?, imageUrl: String?, clickUrl: String?) {
            callback?.onDaastStart(imageUrl, clickUrl)
        }

        override fun daastEnded() {
            callback?.onDaastEnd()
        }

        override fun error(message: String?) {
            callback?.onError("Player error $message")
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
        player.setVolume(volume.toDouble())
    }

    override fun getVolume(): Float {
        return player.getVolume().toFloat()
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