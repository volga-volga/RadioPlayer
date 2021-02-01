package com.likhanov.radioplayer.playback

import com.infteh.startrekplayer.StartrekPlayerDelegate
import com.infteh.startrekplayer.StartrekPlayerQuality
import com.infteh.startrekplayer.StartrekPlayerState
import java.util.*


abstract class RadioPlayerCallback : StartrekPlayerDelegate() {
    override fun ended() {

    }

    override fun error(message: String?) {
    }

    override fun streamUrlChanged(streamUrl: String?) {
    }

    override fun isRestartedChanged(isRestarted: Boolean) {
    }

    override fun isHlsChanged(isHls: Boolean) {
    }

    override fun isSeekableChanged(isSeekable: Boolean) {
    }

    override fun stateChanged(state: StartrekPlayerState?) {
    }

    override fun isPlayingChanged(isPlaying: Boolean) {
    }

    override fun isStalledChanged(isStalled: Boolean) {
    }

    override fun isPausedChanged(isPaused: Boolean) {
    }

    override fun isStoppedChanged(isStopped: Boolean) {
    }

    override fun lengthChanged(length: Double) {
    }

    override fun bufferedLengthChanged(bufferedLength: Double) {
    }

    override fun startPositionChanged(startPosition: Double) {
    }

    override fun positionChanged(position: Double) {
    }

    override fun playbackRateChanged(playbackRate: Double) {
    }

    override fun metaChanged(meta: String?) {
    }

    override fun volumeChanged(volume: Double) {
    }

    override fun duckVolumeChanged(duckVolume: Boolean) {
    }

    override fun playingBitratesChanged(playingBitrates: ArrayList<Int>?) {
    }

    override fun availableBitratesChanged(availableBitrates: ArrayList<Int>?) {
    }

    override fun currentBitrateChanged(currentBitrate: Int) {
    }

    override fun currentQualityChanged(currentQuality: StartrekPlayerQuality?) {
    }

    override fun playingBitrateChanged(playingBitrate: Int) {
    }

    override fun playingQualityChanged(playingQuality: StartrekPlayerQuality?) {
    }

    override fun daastUrlChanged(daastUrl: String?) {
    }

    override fun daastStarted(meta: String?, imageUrl: String?, clickUrl: String?) {
    }

    override fun daastError(message: String?) {
    }

    override fun daastSkipped() {
    }

    override fun daastEnded() {
    }
}