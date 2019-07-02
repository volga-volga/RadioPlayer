package com.likhanov.radioplayer.radio

import android.support.v4.media.session.PlaybackStateCompat
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class RadioStateController {

    private val stateObservable = BehaviorSubject.create<Int>()
    private val requestStateChangeObservable = PublishSubject.create<Long>()

    fun setState(state: Int) {
        stateObservable.onNext(state)
    }

    fun play() {
        requestStateChangeObservable.onNext(PlaybackStateCompat.ACTION_PLAY)
    }

    fun pause() {
        requestStateChangeObservable.onNext(PlaybackStateCompat.ACTION_PAUSE)
    }

    fun getRequestStateChangeObservable(): Observable<Long> = requestStateChangeObservable

    fun getPlaybackStateObservable(): Observable<Int> = stateObservable
}