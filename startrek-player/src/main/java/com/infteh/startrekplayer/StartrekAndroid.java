package com.infteh.startrekplayer;

import android.os.Build;
import android.util.Log;

import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public final class StartrekAndroid {
    public static final List<String> LIBS_ABIS = Arrays.asList(
            "arm64-v8a",
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

    public static ArrayList<byte[]> getSSLCertificates()
    {
        ArrayList<byte[]> certificateList = new ArrayList<byte[]>();

        try {
            TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            factory.init((KeyStore) null);

            for (TrustManager manager : factory.getTrustManagers()) {
                if (manager instanceof X509TrustManager) {
                    X509TrustManager trustManager = (X509TrustManager) manager;

                    for (X509Certificate certificate : trustManager.getAcceptedIssuers()) {
                        byte[] buffer = certificate.getEncoded();
                        certificateList.add(buffer);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("StartrekAndroid", "Failed to get certificates", e);
        }

        return certificateList;
    }
}
