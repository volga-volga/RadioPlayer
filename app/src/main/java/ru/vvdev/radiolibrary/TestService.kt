package ru.vvdev.radiolibrary

import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import com.likhanov.radioplayer.model.NotificationData
import com.likhanov.radioplayer.radio.RadioService
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONArray
import org.json.JSONObject
import java.io.Serializable

class TestService : RadioService() {

    private var socket: Socket? = null
    private var gson = Gson()
    private val mainLooperHandler = Handler(Looper.getMainLooper())

    override fun onCreate() {
        super.onCreate()
        init(this, TestService::class.java)
        updateUrl("http://icecast-studio21.cdnvideo.ru/S21_1")
        setSessionActivity(MainActivity::class.java)
        setActivityForNotificationIntent(MainActivity::class.java)
        initSockets()
    }

    override fun onDestroy() {
        mainLooperHandler.removeCallbacksAndMessages(null)
        socket?.disconnect()
        super.onDestroy()
    }

    private fun initSockets() {
        if (socket == null) {
            socket = IO.socket(SocketEvent.SOCKET_URL)
            socket?.on(SocketEvent.PLAYLIST) {
                val obj = it[0] as JSONArray
                if (obj.length() > 0) {
                    val song = gson.fromJson(obj[0].toString(), Song::class.java)
                    song?.let {
                        mainLooperHandler.post {
                            updateNotification(
                                NotificationData(
                                    it.artist, it.song, it.info?.image ?: ""
                                )
                            )
                        }
                    }
                }
            }?.on(SocketEvent.SONG_BEGIN) {
                val obj = it[0] as JSONObject
                val song = gson.fromJson(obj.toString(), Song::class.java)
                song?.let {
                    mainLooperHandler.post {
                        updateNotification(
                            NotificationData(
                                it.artist, it.song, it.info?.image ?: ""
                            )
                        )
                    }
                }
            }

            socket?.let { if (!it.connected()) it.connect() }
        }
    }
}

data class Song(
    val song: String, val artist: String, val info: Info?,
    val startTime: String, val startTs: Long
) : Serializable

data class Info(
    val song: String,
    val artistName: String,
    val image: String,
    val audio: String,
    val artists: MutableList<Artist>
) : Serializable

data class Artist(val id: String, val name: String) : Serializable

object SocketEvent {
    val SOCKET_URL = "https://mplb.emg.fm/nr"

    val PLAYLIST = "playlist"
    val SONG_BEGIN = "song began"
    val SONG_END = "song ended"
    val GET_PLAYLIST = "get playlist"
}