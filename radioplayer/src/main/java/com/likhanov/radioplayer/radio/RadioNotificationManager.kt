package com.likhanov.radioplayer.radio

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.RemoteException
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.support.v4.media.MediaBrowserServiceCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.app.NotificationCompat.MediaStyle
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.likhanov.radioplayer.R
import com.likhanov.radioplayer.model.NotificationData
import com.likhanov.radioplayer.util.Store
import java.util.*

class RadioNotificationManager(val service: MediaBrowserServiceCompat, val context: Context?) : BroadcastReceiver() {

    private val TAG = "RadioNotification"

    private var sessionToken: MediaSessionCompat.Token? = null
    private var controller: MediaControllerCompat? = null
    private var transportControls: MediaControllerCompat.TransportControls? = null

    private var playbackState: PlaybackStateCompat? = null
    private var metadata: MediaMetadataCompat? = null
    private val notificationManager: NotificationManager =
        service.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val pkg = service.packageName
    private val playIntent: PendingIntent = PendingIntent.getBroadcast(
        service, REQUEST_CODE,
        Intent(ACTION_PLAY).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT
    )
    private val pauseIntent: PendingIntent = PendingIntent.getBroadcast(
        service, REQUEST_CODE,
        Intent(ACTION_PAUSE).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT
    )
    private val stopIntent: PendingIntent = PendingIntent.getBroadcast(
        service, REQUEST_CODE,
        Intent(ACTION_STOP).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT
    )

    var started = false
    private val notificationBuilder = NotificationCompat.Builder(service, CHANNEL_ID)
    private var art = BitmapFactory.decodeResource(service.resources, R.drawable.ic_song_image)
    private var lastBitmap = BitmapFactory.decodeResource(service.resources, R.drawable.ic_song_image)
    private var activity: Class<*>? = null
    private var notificationImage: Int? = null

    companion object {
        private val CHANNEL_ID = "radio_channel_id"

        private val NOTIFICATION_ID = 412
        private val REQUEST_CODE = 100

        const val ACTION_PAUSE = "action_pause"
        const val ACTION_PLAY = "action_play"
        const val ACTION_STOP = "action_stop"
    }

    init {
        updateSessionToken()
        notificationManager.cancelAll()
    }

    fun setDefaultDrawable(drawableRes: Int) {
        art = BitmapFactory.decodeResource(service.resources, drawableRes)
        lastBitmap = BitmapFactory.decodeResource(service.resources, drawableRes)
    }

    fun setActivityForNotificationIntent(activity: Class<*>) {
        this.activity = activity
    }

    fun setNotificationDrawable(drawableRes: Int) {
        this.notificationImage = drawableRes
    }


    fun startNotification() {
        if (!started) {
            metadata = controller?.metadata
            playbackState = controller?.playbackState

            // The notification must be updated after setting started to true
            val notification = createNotification(lastData, null, false)
            if (notification != null) {
                val filter = IntentFilter()
                filter.addAction(ACTION_PAUSE)
                filter.addAction(ACTION_PLAY)
                filter.addAction(ACTION_STOP)
                service.registerReceiver(this, filter)

                service.startForeground(NOTIFICATION_ID, notification)
                started = true
            }
        }
    }

    fun stopNotification() {
        if (started) {
            started = false
            try {
                notificationManager.cancel(NOTIFICATION_ID)
                service.unregisterReceiver(this)
            } catch (ex: IllegalArgumentException) {
                // ignore if the receiver is not registered.
            }

            service.stopForeground(true)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        when (action) {
            ACTION_PAUSE -> transportControls?.pause()
            ACTION_PLAY -> transportControls?.play()
            ACTION_STOP -> stopNotification()
            else -> Log.d(TAG, "Unknown intent ignored. Action=$action")
        }
    }

    private fun createContentIntent(): PendingIntent? {
        activity?.let {
            val openUI = Intent(service, activity)
            openUI.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

            return PendingIntent.getActivity(service, REQUEST_CODE, openUI, PendingIntent.FLAG_CANCEL_CURRENT)
        } ?: return null
    }

    @Throws(RemoteException::class)
    private fun updateSessionToken() {
        val freshToken = service.getSessionToken()
        if (sessionToken == null && freshToken != null || sessionToken != null && sessionToken != freshToken) {
            sessionToken = freshToken
            if (sessionToken != null) {
                controller = MediaControllerCompat(service, sessionToken!!)
                transportControls = controller?.transportControls
            }
        }
    }

    private fun createNotification(data: NotificationData?, bitmap: Bitmap?, forPause: Boolean?): Notification? {
        if (playbackState == null) {
            return null
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        addActions(notificationBuilder, forPause)
        notificationBuilder
            .setStyle(
                MediaStyle()
                    .setShowCancelButton(true)
                    .setCancelButtonIntent(stopIntent)
                    .setMediaSession(sessionToken)
            )
            .setDeleteIntent(stopIntent)
            .setSmallIcon(notificationImage ?: R.drawable.ic_notification)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .setContentIntent(createContentIntent())
            .setContentTitle(data?.artist ?: lastData?.artist ?: "")
            .setDeleteIntent(createOnDismissedIntent())
            .setContentText(lastData?.track ?: lastData?.track ?: "")
            .setLargeIcon(bitmap ?: lastBitmap ?: art)

        return notificationBuilder.build()
    }

    private fun createOnDismissedIntent(): PendingIntent {
        val intent = Intent(context, RadioService.Companion.NotificationDismissedReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent, 0)
        return pendingIntent
    }

    @SuppressLint("RestrictedApi")
    private fun addActions(notificationBuilder: NotificationCompat.Builder, forPause: Boolean?) {
        val oldAction = notificationBuilder.mActions?.firstOrNull()
        notificationBuilder.mActions.clear()

        var icon: Int = R.drawable.ic_pause_black_24dp
        var intent: PendingIntent = pauseIntent

        if (forPause == null) {
            oldAction?.let {
                icon = it.icon
                intent = it.actionIntent
            }
        } else if (!forPause) {
            icon = R.drawable.ic_pause_black_24dp
            intent = pauseIntent
        } else {
            icon = R.drawable.ic_play_arrow_black_24dp
            intent = playIntent
        }

        notificationBuilder.addAction(NotificationCompat.Action(icon, "", intent))
    }

    private var lastData: NotificationData? = null
    @SuppressLint("RestrictedApi")
    fun updateNotification(data: NotificationData?, forPause: Boolean?) {
        Log.d(TAG, "updateNotification")
        try {
            val notification = createNotification(data, null, forPause)
            notificationManager.notify(NOTIFICATION_ID, notification)
            if (data != null)
                lastData?.let {
                    if (it.imagePath != data.imagePath) loadBitmap(data.imagePath)
                } ?: loadBitmap(data.imagePath)
            lastData = data
        } catch (e: Exception) {
            Log.e(TAG, "updateNotification", e)
            //Handler().postDelayed({ updateNotification(data, forPause) }, 1000)
        }
    }

    private fun loadBitmap(path: String?) {
        if (context != null)
            Glide.with(context)
                .asBitmap()
                .load(path)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        lastBitmap = null
                        notificationManager.notify(NOTIFICATION_ID, createNotification(null, null, null))
                    }

                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        lastBitmap = resource
                        notificationManager.notify(NOTIFICATION_ID, createNotification(null, resource, null))
                    }
                })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        var channelName = Store.getString(Store.PREF_CHANNEL)
        if (channelName.isBlank()) {
            Store.storeData(Store.PREF_CHANNEL, UUID.randomUUID().toString())
            channelName = Store.getString(Store.PREF_CHANNEL)
        }
        if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH
            )

            notificationChannel.description = "$channelName description"

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}