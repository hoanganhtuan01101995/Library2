package com.alticast.viettelottcommons.resource.request;

import com.alticast.viettelottcommons.WindmillConfiguration;

/**
 * Created by mc.kim on 8/3/2016.
 */
public class MyDeviceInfo {
    private String id = null;
    private String model = WindmillConfiguration.model;
    private String model_no = null;
    private String os_name = null;
    private String os_version = null;
    private String manufacturer = null;
    private String type = WindmillConfiguration.type;

    public MyDeviceInfo(String id, String model_no, String os_name, String os_version, String manufacturer) {
        this.id = id;
        this.model_no = model_no;
        this.os_name = os_name;
        this.os_version = os_version;
        this.manufacturer = manufacturer;
    }

    public String getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public String getModel_no() {
        return model_no;
    }

    public String getOs_name() {
        return os_name;
    }

    public String getOs_version() {
        return os_version;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getType() {
        return type;
    }
}
