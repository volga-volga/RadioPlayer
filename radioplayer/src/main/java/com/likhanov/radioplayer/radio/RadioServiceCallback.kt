package com.likhanov.radioplayer.radio

interface RadioServiceCallback {
    fun onDaastStart(image: String, link: String)
    fun onDaastEnd()
    fun onDaastError()
}