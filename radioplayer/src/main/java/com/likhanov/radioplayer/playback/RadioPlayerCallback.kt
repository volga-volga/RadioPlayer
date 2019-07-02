package com.likhanov.radioplayer.playback

import org.radiobox.startrek_player.StartrekPlayerDelegate

abstract class RadioPlayerCallback : StartrekPlayerDelegate() {

    override fun onHlsMasterDone() {

    }

    override fun onDaastError() {
    }

    override fun onPause() {
    }

    override fun onMeta(p0: String?) {
    }

    override fun onPlay() {
    }

    override fun onStop() {
    }

    override fun onHlsMasterError() {
    }

    override fun onDaastStart(p0: String?, p1: String?, p2: String?) {
    }

    override fun onDebug(p0: String?) {
    }

    override fun onEnd() {
    }

    override fun onError(p0: String?, p1: Int) {
    }

    override fun onStalled() {
    }

    override fun onDaastEnd() {
    }
}