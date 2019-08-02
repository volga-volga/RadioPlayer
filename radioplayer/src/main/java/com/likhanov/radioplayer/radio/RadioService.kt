package com.likhanov.radioplayer.radio

import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserServiceCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaButtonReceiver
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v7.media.MediaRouter
import android.util.Log
import com.likhanov.radioplayer.model.NotificationData
import com.likhanov.radioplayer.playback.PlaybackManager
import com.likhanov.radioplayer.playback.RadioPlayback
import com.likhanov.radioplayer.util.Store
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference


open class RadioService : MediaBrowserServiceCompat(), PlaybackManager.PlaybackServiceCallback,
    AudioManager.OnAudioFocusChangeListener, RadioServiceCallback {

    init {
        System.loadLibrary("startrek_player")
    }

    private val TAG = "RadioService"

    private lateinit var playbackManager: PlaybackManager
    private lateinit var playback: RadioPlayback
    private lateinit var session: MediaSessionCompat
    private val disposable = CompositeDisposable()
    private lateinit var radioNotificationManager: RadioNotificationManager
    private lateinit var mediaRouter: MediaRouter
    private val radioStateController = RadioStateController()
    private lateinit var delayedStopHandler: DelayedStopHandler
    private lateinit var audioManager: AudioManager
    private var focusRequest: AudioFocusRequest? = null
    private val myNoisyAudioStreamReceiver = NoisyAudioStreamReceiver()
    private val intentFilter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
    private var audioNoisyReceiverRegistered = false
    private var lastData: NotificationData? = null
    private lateinit var service: MediaBrowserServiceCompat
    private lateinit var serviceClass: Class<*>

    companion object {
        const val MEDIA_ID_ROOT = "__ROOT__"
        const val ACTION_CMD = "com.example.android.uamp.ACTION_CMD"
        const val CMD_NAME = "CMD_NAME"
        const val CMD_PAUSE = "CMD_PAUSE"
        private const val STOP_DELAY = 3000
        private var needUpdateNotification = false

        class NotificationDismissedReceiver : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                needUpdateNotification = false
            }
        }

    }

    override fun init(service: MediaBrowserServiceCompat, serviceClass: Class<*>) {
        Store.init(applicationContext)
        this.service = service
        this.serviceClass = serviceClass

        delayedStopHandler = DelayedStopHandler(service)
        playback = RadioPlayback("")
        playbackManager = PlaybackManager(playback, this)

        session = MediaSessionCompat(applicationContext, TAG)
        sessionToken = session.sessionToken
        session.setCallback(playbackManager.mediaSessionCallback)
        session.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)

        radioNotificationManager = RadioNotificationManager(service, baseContext)
        mediaRouter = MediaRouter.getInstance(applicationContext)

        playbackManager.updatePlaybackState(null)

        disposable.add(
            radioStateController.getRequestStateChangeObservable().subscribe {
                if (it == PlaybackStateCompat.ACTION_PLAY) {
                    playbackManager.handlePlayRequest()
                } else if (it == PlaybackStateCompat.ACTION_PAUSE) {
                    playbackManager.handlePauseRequest()
                }
            }
        )
        initAudioManager()
    }

    override fun onStartCommand(startIntent: Intent?, flags: Int, startId: Int): Int {
        if (startIntent != null) {
            val action = startIntent.action
            val command = startIntent.getStringExtra(CMD_NAME)
            if (ACTION_CMD == action) {
                if (CMD_PAUSE == command) {
                    playbackManager.handlePauseRequest()
                }
            } else MediaButtonReceiver.handleIntent(session, startIntent)
        }

        delayedStopHandler.removeCallbacksAndMessages(null)
        delayedStopHandler.sendEmptyMessageDelayed(0, STOP_DELAY.toLong())
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterAudioNoisyReceiver()
        radioNotificationManager.stopNotification()
        delayedStopHandler.removeCallbacksAndMessages(null)
        session.release()
        disposable.dispose()
    }

    override fun isPlaying() = playback.playing()

    override fun isPaused() = playback.isPaused()

    override fun isRestarted() = playback.isRestarted()

    override fun updateUrl(url: String, masterStream: Boolean) = playback.updateUrl(url, masterStream)

    override fun setAd(url: String) = playback.setAd(url)

    override fun setSessionActivity(activity: Class<*>) {
        val intent = Intent(applicationContext, activity)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 99, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        session.setSessionActivity(pendingIntent)
    }

    override fun updateNotification(data: NotificationData?) {
        Log.d(TAG, "updateNotification")
        data?.let {
            lastData = data
            if (needUpdateNotification) radioNotificationManager.updateNotification(data, null)
        }
    }

    override fun setDefaultDrawable(drawableRes: Int) = radioNotificationManager.setDefaultDrawable(drawableRes)

    override fun setActivityForNotificationIntent(activity: Class<*>) =
        radioNotificationManager.setActivityForNotificationIntent(activity)

    override fun setNotificationDrawable(drawableRes: Int) =
        radioNotificationManager.setNotificationDrawable(drawableRes)

    override fun onPlaybackStart() {
        session.isActive = true

        delayedStopHandler.removeCallbacksAndMessages(null)
        serviceClass?.let { startService(Intent(applicationContext, it)) }
    }

    override fun onNotificationRequired() {
        needUpdateNotification = true
        radioNotificationManager.startNotification()
    }

    override fun onPlaybackStop() {
        Log.d(TAG, "onPlaybackStop")
        session.isActive = false

        delayedStopHandler.removeCallbacksAndMessages(null)
        delayedStopHandler.sendEmptyMessageDelayed(0, STOP_DELAY.toLong())

        stopForeground(false)
        radioNotificationManager.started = false
        if(needUpdateNotification) radioNotificationManager.updateNotification(lastData, true)
        needUpdateNotification = false
        unregisterAudioNoisyReceiver()
    }

    override fun onPlaybackStateUpdated(state: PlaybackStateCompat) {
        if (state.state == PlaybackStateCompat.STATE_PLAYING) registerAudioNoisyReceiver()
        session.setPlaybackState(state)
        radioStateController.setState(state.state)
    }

    override fun onPlaybackMetadataUpdated(metadata: MediaMetadataCompat) {
        session.setMetadata(metadata)
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {

    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot? = BrowserRoot(
        MEDIA_ID_ROOT, null
    )

    override fun onTaskRemoved(rootIntent: Intent?) {
        if (playbackManager != null && !playbackManager.playback.isPlaying)
            stopSelf()
    }

    private class DelayedStopHandler(service: Service) : Handler() {
        private val weakReference: WeakReference<Service> = WeakReference<Service>(service)

        override fun handleMessage(msg: Message) {
        }
    }


    private fun initAudioManager() {
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(
                    AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAcceptsDelayedFocusGain(true)
                .setOnAudioFocusChangeListener(this@RadioService, Handler())
                .build()
        }
    }

    private fun registerAudioNoisyReceiver() {
        try {
            if (!audioNoisyReceiverRegistered) {
                registerReceiver(myNoisyAudioStreamReceiver, intentFilter)
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
                    audioManager.requestAudioFocus(
                        this@RadioService,
                        AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN
                    )
                else {
                    focusRequest?.let { audioManager.requestAudioFocus(it) }
                }
                audioNoisyReceiverRegistered = true
            }
        } catch (e: Exception) {

        }
    }

    private fun unregisterAudioNoisyReceiver() {
        try {
            if (audioNoisyReceiverRegistered) {
                unregisterReceiver(myNoisyAudioStreamReceiver)
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
                    audioManager.abandonAudioFocus(this@RadioService)
                else focusRequest?.let { audioManager.abandonAudioFocusRequest(it) }
                audioNoisyReceiverRegistered = false
            }
        } catch (e: Exception) {

        }
    }

    private inner class NoisyAudioStreamReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY == intent.action) {
                if (playbackManager != null) playbackManager.handlePauseRequest()
            }
        }
    }

    override fun onAudioFocusChange(focusChange: Int) {
        if (focusChange == AudioManager.AUDIOFOCUS_LOSS ||
            focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT
        )
            if (playbackManager != null) playbackManager.handlePauseRequest()
    }
}