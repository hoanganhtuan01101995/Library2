package com.alticast.viettelottcommons.resource;

import java.util.ArrayList;

public class RegisterTagDevice {

    String applicationId;         //":"APPLICATION_CODE",
    String hwid;                //":"hardware device id",
    TagPushWood tags;

    public TagPushWood getTags() {
        return tags;
    }

    public void setTags(TagPushWood tags) {
        this.tags = tags;
    }

    public String getApplication() {
        return applicationId;
    }

    public void setApplication(String application) {
        this.applicationId = application;
    }


    public String getHwid() {
        return hwid;
    }

    public void setHwid(String hwid) {
        this.hwid = hwid;
    }

}
