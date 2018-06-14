package com.alticast.viettelottcommons.resource.request;

/**
 * Created by mc.kim on 8/3/2016.
 */
public class CreateAccountReq {
    private String id = null;
    private String password = null;
    private String code = null;
    private String client_id = null;
    private String hash = null;
    private String cellphone = null;
    private String gender = null;
    private String email = null;
    private String address = null;
    private String region = null;
    private String name = null;
    private String display_name = null;
    private MyDeviceInfo device = null;


    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public void setDevice(MyDeviceInfo device) {
        this.device = device;
    }
}
