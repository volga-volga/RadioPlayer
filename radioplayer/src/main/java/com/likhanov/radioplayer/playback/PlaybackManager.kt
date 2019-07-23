package com.likhanov.radioplayer.playback

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.text.TextUtils
import android.util.Log
import com.likhanov.radioplayer.util.extensions.hasInternetConnection

class PlaybackManager(val playback: Playback, private val serviceCallback: PlaybackServiceCallback) :
    Playback.Callback {

    val TAG = "playback_manager"

    companion object {
        const val VOLUME_CUSTOM_ACTION = "volume_custom_action"
        private const val VOLUME_BUNDLE = "volume_bundle"

        fun createVolumeArgs(volume: Float): Bundle {
            val args = Bundle()
            args.putFloat(VOLUME_BUNDLE, volume)

            return args
        }

        fun getVolumeFromArgs(args: Bundle?): Float {
            if (args == null) return 0F

            return args.getFloat(VOLUME_BUNDLE, 0f)
        }
    }

    init {
        playback.setCallback(this)
    }

    val mediaSessionCallback: MediaSessionCompat.Callback = object : MediaSessionCompat.Callback() {
        override fun onPlay() {
            handlePlayRequest()
        }

        override fun onSeekTo(pos: Long) {
            playback.seekTo(pos)
        }

        override fun onPause() {
            handlePauseRequest()
        }

        override fun onStop() {
            handleStopRequest(null)
        }

        override fun onCustomAction(action: String?, extras: Bundle?) {
            if (TextUtils.equals(action, VOLUME_CUSTOM_ACTION) && extras != null) {
                playback.volume = getVolumeFromArgs(extras)
            }
        }
    }

    @SuppressLint("CheckResult")
    fun handlePlayRequest() {
        hasInternetConnection().subscribe({
            if (it) {
                Log.d("stateTag", "handlePlayRequest")
                serviceCallback.onPlaybackStart()
                playback.play()
            }
        }, Throwable::printStackTrace)
    }

    @SuppressLint("CheckResult")
    fun handlePauseRequest() {
        hasInternetConnection().subscribe({
            if (it) {
                Log.d("stateTag", "handlePauseRequest")
                if (playback.isPlaying) {
                    playback.pause()
                    serviceCallback.onPlaybackStop()
                }
            } else handleStopRequest(null)
        }, Throwable::printStackTrace)
    }

    fun handleStopRequest(withError: String?) {
        playback.state = PlaybackStateCompat.STATE_PAUSED
        updatePlaybackState(null)
        playback.stop(true)
        serviceCallback.forceStop()
    }

    fun updatePlaybackState(error: String?) {
        var position = PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN
        if (playback.isConnected) {
            position = playback.currentStreamPosition
        }

        val stateBuilder = PlaybackStateCompat.Builder()
            .setActions(getAvailableActions())

        var state = playback.state

        // If there is an error message, send it to the playback state:
        if (error != null) {
            // Error states are really only supposed to be used for errors that cause playback to
            // stop unexpectedly and persist until the user takes action to fix it.
            stateBuilder.setErrorMessage(error)
            state = PlaybackStateCompat.STATE_ERROR
        }

        stateBuilder.setState(state, position, 1.0f, SystemClock.elapsedRealtime())
        stateBuilder.setExtras(createVolumeArgs(playback.volume))

        serviceCallback.onPlaybackStateUpdated(stateBuilder.build())

        if (state == PlaybackStateCompat.STATE_PLAYING || state == PlaybackStateCompat.STATE_PAUSED) {
            serviceCallback.onNotificationRequired()
        }
    }

    private fun getAvailableActions(): Long {
        var actions = PlaybackStateCompat.ACTION_PLAY_PAUSE

        if (playback.isPlaying()) {
            actions = actions or PlaybackStateCompat.ACTION_PAUSE
        } else {
            actions = actions or PlaybackStateCompat.ACTION_PLAY
        }

        return actions
    }

    override fun onCompletion() {
        handleStopRequest(null)
    }

    override fun onPlaybackStatusChanged(state: Int) {
        playback.state = state
        updatePlaybackState(null)
    }

    override fun onError(error: String?) {
        updatePlaybackState(error)
    }

    interface PlaybackServiceCallback {
        fun onPlaybackStart()

        fun onNotificationRequired()

        fun onPlaybackStop()

        fun onPlaybackStateUpdated(state: PlaybackStateCompat)

        fun onPlaybackMetadataUpdated(metadata: MediaMetadataCompat)

        fun forceStop()
    }
}