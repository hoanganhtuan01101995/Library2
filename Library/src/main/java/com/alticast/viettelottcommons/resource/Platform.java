package com.alticast.viettelottcommons.resource;

/**
 * Created by mc.kim on 8/10/2016.
 */
public class Platform {
    private String id = null;
    private Config[] config = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Config[] getConfig() {
        return config;
    }

    public void setConfig(Config[] config) {
        this.config = config;
    }

    public String getValue(String name) {
        String value = null;
        if (config != null) {
            int size = config.length;
            for (int i = 0; i < size; i++) {
                if (config[i].getName().equals(name)) {
                    value = config[i].getValue();
                    break;
                }
            }
        }
        return value;
    }

}
