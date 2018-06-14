package com.alticast.viettelottcommons.resource;

import java.util.ArrayList;

/**
 * Created by mc.kim on 8/8/2016.
 */
public class Location {
    private String device = null;
    private String format = null;
    private String provider = null;
    private float duration = 0;

    private boolean encryption = false;

    private String locator = null;

    private String resolution = null;

    private int bitrate = 0;

    private ArrayList<Parameter> parameter;

    public String getDevice() {
        return device;
    }

    public String getFormat() {
        return format;
    }

    public String getProvider() {
        return provider;
    }

    public float getDuration() {
        return duration;
    }

    public boolean isEncryption() {
        return encryption;
    }

    public String getLocator() {
        return locator;
    }

    public String getResolution() {
        return resolution;
    }

    public int getBitrate() {
        return bitrate;
    }

    public ArrayList<Parameter> getParameter() {
        return parameter;
    }

    public Parameter getParameter(String type) {
        if(parameter == null) {
            return null;
        }
        int size = parameter.size();
        for (int i = 0; i < size; i++) {
            if (parameter.get(i).getName().equalsIgnoreCase(type)) {
                return parameter.get(i);
            }
        }
        return parameter.get(0);
    }

    final String AUDIENCE = "Audience";
    final String FREE = "private:All";

    public boolean isFlag(){
        if(parameter == null){
            return false;
        }else{
            int size = parameter.size();
            String value = null;
            for(int i =  0 ; i < size ; i  ++){
                if(parameter.get(i).getName().equals(AUDIENCE)){
                    value = parameter.get(i).getValue();
                    break;
                }
            }

            if(value == null){
                return false;
            }else{
                if(value.equals(FREE)){
                    return true;
                }else{
                    return false;
                }
            }
        }
    }
}
