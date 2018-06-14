package com.alticast.viettelottcommons.event;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by mc.kim on 9/2/2016.
 */
public class LocalEventBus {
    private static LocalEventBus ourInstance = null;
    private LocalBroadcastManager mLocalBroadcastManager = null;
    private static Context mContext = null;

    public static LocalEventBus getInstance(Context context) {
        if (ourInstance == null) {
            mContext = context;
            ourInstance = new LocalEventBus(context);
        }
        return ourInstance;
    }

    private LocalEventBus(Context context) {
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    public void subscribe(Class eventcls, BroadcastReceiver broadcastReceiver) {
        IntentFilter intentFilter = new IntentFilter(eventcls.getName());
        mLocalBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    public void unSubscribe(BroadcastReceiver broadcastReceiver) {
        mLocalBroadcastManager.unregisterReceiver(broadcastReceiver);
    }

    public void sendMessage(IEvent iEvent) {
        mLocalBroadcastManager.sendBroadcast(iEvent.getIntent());
    }
}
