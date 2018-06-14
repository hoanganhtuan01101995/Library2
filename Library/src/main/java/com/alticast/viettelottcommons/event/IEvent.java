package com.alticast.viettelottcommons.event;

import android.content.Intent;

/**
 * Created by mc.kim on 9/2/2016.
 */
public interface IEvent {
    public Intent getIntent();

    public Class getClassType();
}
