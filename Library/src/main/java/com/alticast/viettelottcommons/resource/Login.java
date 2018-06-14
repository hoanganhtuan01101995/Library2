package com.alticast.viettelottcommons.resource;

/**
 * Created by mc.kim on 7/28/2016.
 */
public class Login extends ApiError {

    private String access_token = null;
    private String token_secret = null;
    private String expiration_date = null;
    private String refresh_token = null;
    private String refresh_token_expiration_date = null;
    private String message = null;
    private String status = null;
    private boolean temp_password = false;
    private RegistedDevice registered_device;

    public boolean isLogined() {
        return isLogined;
    }

    public void setLogined(boolean logined) {
        isLogined = logined;
    }

    private boolean isLogined = false;

    public RegistedDevice getRegistered_device() {
        return registered_device;
    }

    public void setRegistered_device(RegistedDevice registered_device) {
        this.registered_device = registered_device;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public String getRefresh_token_expiration_date() {
        return refresh_token_expiration_date;
    }

    public boolean isTemp_password() {
        return temp_password;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getToken_secret() {
        return token_secret;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExpiration_date() {
        return expiration_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
