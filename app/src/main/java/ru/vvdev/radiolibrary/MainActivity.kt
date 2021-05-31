package ru.vvdev.radiolibrary

import android.annotation.SuppressLint
import android.content.ComponentName
import android.os.Bundle
import android.os.RemoteException
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var mediaBrowser: MediaBrowserCompat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mediaBrowser =
            MediaBrowserCompat(this, ComponentName(this, TestService::class.java), mConnectionCallback, null)

        ivAction.setOnClickListener { onActionClicked() }
    }

    override fun onStart() {
        super.onStart()
        mediaBrowser?.connect()
    }

    override fun onStop() {
        super.onStop()
        val controllerCompat = MediaControllerCompat.getMediaController(this)
        controllerCompat?.unregisterCallback(mediaControllerCallback)
        mediaBrowser?.disconnect()
    }

    private val mediaControllerCallback = object : MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackStateCompat) {}

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {}
    }

    private val mConnectionCallback = object : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            try {
                mediaBrowser?.sessionToken?.let { connectToSession(it) }
            } catch (e: RemoteException) {
                e.printStackTrace()
            }

        }
    }

    @Throws(RemoteException::class)
    private fun connectToSession(token: MediaSessionCompat.Token) {
        val mediaController = MediaControllerCompat(this, token)
        MediaControllerCompat.setMediaController(this, mediaController)
        onMediaControllerConnected()
    }

    protected fun onMediaControllerConnected() {
        val controller = MediaControllerCompat.getMediaController(this)

        if (controller != null) {
            onPlaybackStateChanged(controller.playbackState)
            controller.registerCallback(callback)
        }
    }

    private val callback = object : MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
            this@MainActivity.onPlaybackStateChanged(state)
        }
    }

    private fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
        Log.d("stateTag", "onPlaybackStateChanged(), state = ${state?.state ?: "null"}")
        if (state == null) {
            return
        }

        var enablePlay = false
        when (state.state) {
            PlaybackStateCompat.STATE_PAUSED, PlaybackStateCompat.STATE_STOPPED, PlaybackStateCompat.STATE_NONE -> enablePlay =
                true
            PlaybackStateCompat.STATE_ERROR -> {
                enablePlay = true
            }
        }

        if (enablePlay) {
            ivAction.text = "play"
        } else {
            ivAction.text = "pause"
        }
    }

    private fun onActionClicked() {
        try {
            val controller = MediaControllerCompat.getMediaController(this)
            val stateObj = controller.playbackState
            val state = stateObj?.state ?: PlaybackStateCompat.STATE_NONE
            Log.d("stateTag", "state = $state")
            when (state) {
                PlaybackStateCompat.STATE_PAUSED, PlaybackStateCompat.STATE_STOPPED, PlaybackStateCompat.STATE_NONE -> {
                    playMedia()
                }
                PlaybackStateCompat.STATE_PLAYING, PlaybackStateCompat.STATE_BUFFERING, PlaybackStateCompat.STATE_CONNECTING -> pauseMedia()
                PlaybackStateCompat.STATE_ERROR -> playMedia()
                else -> {
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("CheckResult")
    private fun playMedia() {
        val controller = MediaControllerCompat.getMediaController(this)
        controller?.transportControls?.play()
    }

    @SuppressLint("CheckResult")
    private fun pauseMedia() {
        val controller = MediaControllerCompat.getMediaController(this)
        controller?.transportControls?.pause()
    }

    override fun onBackPressed() {
        finish()
    }
}
