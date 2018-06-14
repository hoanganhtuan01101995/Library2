package com.alticast.viettelottcommons.resource;

public class TagPushWood {

    String StringTag;//": "string value",
    String IntegerTag;//": 42,
    String ListTag;//": ["string1","string2"],
    String DateTag;//": "2015-10-02 22:11", //note the time is in UTC
    String BooleanTag;//": true,  // valid values are - true, 1, false, 0, null

    public String getStringTag() {
        return StringTag;
    }

    public void setStringTag(String stringTag) {
        StringTag = stringTag;
    }

    public String getIntegerTag() {
        return IntegerTag;
    }

    public void setIntegerTag(String integerTag) {
        IntegerTag = integerTag;
    }

    public String getListTag() {
        return ListTag;
    }

    public void setListTag(String listTag) {
        ListTag = listTag;
    }

    public String getDateTag() {
        return DateTag;
    }

    public void setDateTag(String dateTag) {
        DateTag = dateTag;
    }

    public String getBooleanTag() {
        return BooleanTag;
    }

    public void setBooleanTag(String booleanTag) {
        BooleanTag = booleanTag;
    }
}