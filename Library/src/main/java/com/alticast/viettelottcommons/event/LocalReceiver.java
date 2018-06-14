package com.alticast.viettelottcommons.event;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by mc.kim on 9/2/2016.
 */
public abstract class LocalReceiver<T> extends BroadcastReceiver {


    public abstract void onEvent(T event);

    @Override
    public void onReceive(Context context, Intent intent) {
        String className = intent.getStringExtra("event");
        T event = (T) intent.getSerializableExtra(className);
        onEvent(event);
    }
}
