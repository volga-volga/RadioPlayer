// AUTOGENERATED FILE - DO NOT MODIFY!
// This file generated by Djinni from StartrekPlayer.djinni

package com.infteh.startrekplayer;

import android.os.Build;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class StartrekPlayer {
    public static final List<String> LIBS_ABIS = Arrays.asList("arm64-v8a",
            "armeabi-v7a",
            "x86",
            "x86_64");
    public static String PREFERRED_ABI;

    static {
        PREFERRED_ABI = "";
        for (String abi : Build.SUPPORTED_ABIS) {
            if (LIBS_ABIS.contains(abi)) {
                PREFERRED_ABI = "_" + abi;
                break;
            }
        }
        System.loadLibrary("Qt5Core" + PREFERRED_ABI);
    }

    public abstract void deleteDelegate();

    public abstract void setDelegate(StartrekPlayerDelegate delegate);

    /** Player */
    public abstract String daastUrl();

    public abstract String streamUrl();

    public abstract boolean isRestarted();

    public abstract boolean isHls();

    public abstract boolean isSeekable();

    public abstract StartrekPlayerState state();

    public abstract boolean isPlaying();

    public abstract boolean isStalled();

    public abstract boolean isPaused();

    public abstract boolean isStopped();

    public abstract double length();

    public abstract double bufferedLength();

    public abstract double startPosition();

    public abstract double position();

    public abstract double playbackRate();

    public abstract String meta();

    public abstract double volume();

    public abstract boolean duckVolume();

    public abstract ArrayList<String> streamsByBitrate(int bitrate);

    public abstract ArrayList<Integer> playingBitrates();

    public abstract ArrayList<Integer> availableBitrates();

    public abstract int currentBitrate();

    public abstract StartrekPlayerQuality currentQuality();

    public abstract int playingBitrate();

    public abstract StartrekPlayerQuality playingQuality();

    public abstract void setDaastUrl(String daastUrl);

    public abstract void setStreamUrl(String streamUrl);

    public abstract void setIsRestarted(boolean isRestarted);

    public abstract void setPosition(double position);

    public abstract void setPlaybackRate(double playbackRate);

    public abstract void setVolume(double volume);

    public abstract void setDuckVolume(boolean duckVolume);

    public abstract void setPlayingBitrate(int playingBitrate);

    public abstract void setPlayingQuality(StartrekPlayerQuality playingQuality);

    public abstract void daastClick();

    public abstract void playUrl(String url);

    public abstract void play();

    public abstract void pause();

    public abstract void stop();

    public abstract void skipForward(double duration);

    public abstract void skipBackward(double duration);

    public static StartrekPlayer create()
    {
        return CppProxy.create();
    }

    private static final class CppProxy extends StartrekPlayer
    {
        private final long nativeRef;
        private final AtomicBoolean destroyed = new AtomicBoolean(false);

        private CppProxy(long nativeRef)
        {
            if (nativeRef == 0) throw new RuntimeException("nativeRef is zero");
            this.nativeRef = nativeRef;
        }

        private native void nativeDestroy(long nativeRef);
        public void _djinni_private_destroy()
        {
            boolean destroyed = this.destroyed.getAndSet(true);
            if (!destroyed) nativeDestroy(this.nativeRef);
        }
        protected void finalize() throws java.lang.Throwable
        {
            _djinni_private_destroy();
            super.finalize();
        }

        @Override
        public void deleteDelegate()
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            native_deleteDelegate(this.nativeRef);
        }
        private native void native_deleteDelegate(long _nativeRef);

        @Override
        public void setDelegate(StartrekPlayerDelegate delegate)
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            native_setDelegate(this.nativeRef, delegate);
        }
        private native void native_setDelegate(long _nativeRef, StartrekPlayerDelegate delegate);

        @Override
        public String daastUrl()
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            return native_daastUrl(this.nativeRef);
        }
        private native String native_daastUrl(long _nativeRef);

        @Override
        public String streamUrl()
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            return native_streamUrl(this.nativeRef);
        }
        private native String native_streamUrl(long _nativeRef);

        @Override
        public boolean isRestarted()
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            return native_isRestarted(this.nativeRef);
        }
        private native boolean native_isRestarted(long _nativeRef);

        @Override
        public boolean isHls()
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            return native_isHls(this.nativeRef);
        }
        private native boolean native_isHls(long _nativeRef);

        @Override
        public boolean isSeekable()
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            return native_isSeekable(this.nativeRef);
        }
        private native boolean native_isSeekable(long _nativeRef);

        @Override
        public StartrekPlayerState state()
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            return native_state(this.nativeRef);
        }
        private native StartrekPlayerState native_state(long _nativeRef);

        @Override
        public boolean isPlaying()
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            return native_isPlaying(this.nativeRef);
        }
        private native boolean native_isPlaying(long _nativeRef);

        @Override
        public boolean isStalled()
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            return native_isStalled(this.nativeRef);
        }
        private native boolean native_isStalled(long _nativeRef);

        @Override
        public boolean isPaused()
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            return native_isPaused(this.nativeRef);
        }
        private native boolean native_isPaused(long _nativeRef);

        @Override
        public boolean isStopped()
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            return native_isStopped(this.nativeRef);
        }
        private native boolean native_isStopped(long _nativeRef);

        @Override
        public double length()
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            return native_length(this.nativeRef);
        }
        private native double native_length(long _nativeRef);

        @Override
        public double bufferedLength()
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            return native_bufferedLength(this.nativeRef);
        }
        private native double native_bufferedLength(long _nativeRef);

        @Override
        public double startPosition()
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            return native_startPosition(this.nativeRef);
        }
        private native double native_startPosition(long _nativeRef);

        @Override
        public double position()
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            return native_position(this.nativeRef);
        }
        private native double native_position(long _nativeRef);

        @Override
        public double playbackRate()
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            return native_playbackRate(this.nativeRef);
        }
        private native double native_playbackRate(long _nativeRef);

        @Override
        public String meta()
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            return native_meta(this.nativeRef);
        }
        private native String native_meta(long _nativeRef);

        @Override
        public double volume()
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            return native_volume(this.nativeRef);
        }
        private native double native_volume(long _nativeRef);

        @Override
        public boolean duckVolume()
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            return native_duckVolume(this.nativeRef);
        }
        private native boolean native_duckVolume(long _nativeRef);

        @Override
        public ArrayList<String> streamsByBitrate(int bitrate)
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            return native_streamsByBitrate(this.nativeRef, bitrate);
        }
        private native ArrayList<String> native_streamsByBitrate(long _nativeRef, int bitrate);

        @Override
        public ArrayList<Integer> playingBitrates()
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            return native_playingBitrates(this.nativeRef);
        }
        private native ArrayList<Integer> native_playingBitrates(long _nativeRef);

        @Override
        public ArrayList<Integer> availableBitrates()
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            return native_availableBitrates(this.nativeRef);
        }
        private native ArrayList<Integer> native_availableBitrates(long _nativeRef);

        @Override
        public int currentBitrate()
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            return native_currentBitrate(this.nativeRef);
        }
        private native int native_currentBitrate(long _nativeRef);

        @Override
        public StartrekPlayerQuality currentQuality()
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            return native_currentQuality(this.nativeRef);
        }
        private native StartrekPlayerQuality native_currentQuality(long _nativeRef);

        @Override
        public int playingBitrate()
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            return native_playingBitrate(this.nativeRef);
        }
        private native int native_playingBitrate(long _nativeRef);

        @Override
        public StartrekPlayerQuality playingQuality()
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            return native_playingQuality(this.nativeRef);
        }
        private native StartrekPlayerQuality native_playingQuality(long _nativeRef);

        @Override
        public void setDaastUrl(String daastUrl)
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            native_setDaastUrl(this.nativeRef, daastUrl);
        }
        private native void native_setDaastUrl(long _nativeRef, String daastUrl);

        @Override
        public void setStreamUrl(String streamUrl)
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            native_setStreamUrl(this.nativeRef, streamUrl);
        }
        private native void native_setStreamUrl(long _nativeRef, String streamUrl);

        @Override
        public void setIsRestarted(boolean isRestarted)
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            native_setIsRestarted(this.nativeRef, isRestarted);
        }
        private native void native_setIsRestarted(long _nativeRef, boolean isRestarted);

        @Override
        public void setPosition(double position)
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            native_setPosition(this.nativeRef, position);
        }
        private native void native_setPosition(long _nativeRef, double position);

        @Override
        public void setPlaybackRate(double playbackRate)
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            native_setPlaybackRate(this.nativeRef, playbackRate);
        }
        private native void native_setPlaybackRate(long _nativeRef, double playbackRate);

        @Override
        public void setVolume(double volume)
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            native_setVolume(this.nativeRef, volume);
        }
        private native void native_setVolume(long _nativeRef, double volume);

        @Override
        public void setDuckVolume(boolean duckVolume)
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            native_setDuckVolume(this.nativeRef, duckVolume);
        }
        private native void native_setDuckVolume(long _nativeRef, boolean duckVolume);

        @Override
        public void setPlayingBitrate(int playingBitrate)
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            native_setPlayingBitrate(this.nativeRef, playingBitrate);
        }
        private native void native_setPlayingBitrate(long _nativeRef, int playingBitrate);

        @Override
        public void setPlayingQuality(StartrekPlayerQuality playingQuality)
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            native_setPlayingQuality(this.nativeRef, playingQuality);
        }
        private native void native_setPlayingQuality(long _nativeRef, StartrekPlayerQuality playingQuality);

        @Override
        public void daastClick()
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            native_daastClick(this.nativeRef);
        }
        private native void native_daastClick(long _nativeRef);

        @Override
        public void playUrl(String url)
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            native_playUrl(this.nativeRef, url);
        }
        private native void native_playUrl(long _nativeRef, String url);

        @Override
        public void play()
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            native_play(this.nativeRef);
        }
        private native void native_play(long _nativeRef);

        @Override
        public void pause()
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            native_pause(this.nativeRef);
        }
        private native void native_pause(long _nativeRef);

        @Override
        public void stop()
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            native_stop(this.nativeRef);
        }
        private native void native_stop(long _nativeRef);

        @Override
        public void skipForward(double duration)
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            native_skipForward(this.nativeRef, duration);
        }
        private native void native_skipForward(long _nativeRef, double duration);

        @Override
        public void skipBackward(double duration)
        {
            assert !this.destroyed.get() : "trying to use a destroyed object";
            native_skipBackward(this.nativeRef, duration);
        }
        private native void native_skipBackward(long _nativeRef, double duration);

        public static native StartrekPlayer create();
    }
}