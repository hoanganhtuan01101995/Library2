package com.alticast.viettelottcommons.event;

import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;

/**
 * Created by mc.kim on 9/2/2016.
 */
public class EventData implements IEvent, Serializable {
    private String data = null;


    public void setData(String data) {
        this.data = data;
    }

    @Override
    public Intent getIntent() {
        Intent returnIntent = new Intent(this.getClass().getName());
        Bundle bundle = new Bundle();
        bundle.putString("event", EventData.class.getName());
        bundle.putSerializable(EventData.class.getName(), this);

        returnIntent.putExtras(bundle);
        return returnIntent;
    }

    @Override
    public Class getClassType() {
        return null;
    }
}
