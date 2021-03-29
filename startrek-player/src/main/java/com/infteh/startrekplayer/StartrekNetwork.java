// AUTOGENERATED FILE - DO NOT MODIFY!
// This file generated by Djinni from StartrekPlayer.djinni

package com.infteh.startrekplayer;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class StartrekNetwork {
    public static int netTimeoutMs()
    {
        return CppProxy.netTimeoutMs();
    }

    public static int netBuffer()
    {
        return CppProxy.netBuffer();
    }

    public static int netPrebuf()
    {
        return CppProxy.netPrebuf();
    }

    public static boolean netPrebufWait()
    {
        return CppProxy.netPrebufWait();
    }

    public static String userAgent()
    {
        return CppProxy.userAgent();
    }

    public static String referer()
    {
        return CppProxy.referer();
    }

    public static String statisticaUserId()
    {
        return CppProxy.statisticaUserId();
    }

    public static String statisticaReferer()
    {
        return CppProxy.statisticaReferer();
    }

    public static void setNetTimeoutMs(int netTimeoutMs)
    {
        CppProxy.setNetTimeoutMs(netTimeoutMs);
    }

    public static void setNetBuffer(int netBuffer)
    {
        CppProxy.setNetBuffer(netBuffer);
    }

    public static void setNetPrebuf(int netPrebuf)
    {
        CppProxy.setNetPrebuf(netPrebuf);
    }

    public static void setNetPrebufWait(boolean netPrebufWait)
    {
        CppProxy.setNetPrebufWait(netPrebufWait);
    }

    public static void setUserAgent(String userAgent)
    {
        CppProxy.setUserAgent(userAgent);
    }

    public static void setReferer(String referer)
    {
        CppProxy.setReferer(referer);
    }

    public static void setStatisticaUserId(String statisticaUserId)
    {
        CppProxy.setStatisticaUserId(statisticaUserId);
    }

    public static void setStatisticaReferer(String statisticaReferer)
    {
        CppProxy.setStatisticaReferer(statisticaReferer);
    }

    public static void setCaCertificates(ArrayList<byte[]> certificates)
    {
        CppProxy.setCaCertificates(certificates);
    }

    private static final class CppProxy extends StartrekNetwork
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

        public static native int netTimeoutMs();

        public static native int netBuffer();

        public static native int netPrebuf();

        public static native boolean netPrebufWait();

        public static native String userAgent();

        public static native String referer();

        public static native String statisticaUserId();

        public static native String statisticaReferer();

        public static native void setNetTimeoutMs(int netTimeoutMs);

        public static native void setNetBuffer(int netBuffer);

        public static native void setNetPrebuf(int netPrebuf);

        public static native void setNetPrebufWait(boolean netPrebufWait);

        public static native void setUserAgent(String userAgent);

        public static native void setReferer(String referer);

        public static native void setStatisticaUserId(String statisticaUserId);

        public static native void setStatisticaReferer(String statisticaReferer);

        public static native void setCaCertificates(ArrayList<byte[]> certificates);
    }
}
