package com.alticast.viettelottcommons.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.alticast.viettelottcommons.WindmillConfiguration;

/**
 * Created by duyuno on 10/28/16.
 */
public class NetworkUtil {
    public static enum NetworkType {
        DISCONNECT,
        WIFI,
        MOBILE
    }

    public static NetworkType checkNetwork(Context context) {

        if(context == null) return NetworkType.DISCONNECT;

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetInfo = manager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return NetworkType.DISCONNECT;
        } else {
            if(WindmillConfiguration.NETWORK_FAKE) {
                return WindmillConfiguration.is3gMode ? NetworkType.MOBILE : NetworkType.WIFI;
            }
            if(activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return NetworkType.MOBILE;
            } else {
                return NetworkType.WIFI;
            }
        }
    }
}
