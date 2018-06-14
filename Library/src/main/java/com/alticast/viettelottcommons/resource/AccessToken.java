package com.alticast.viettelottcommons.resource;

/**
 * Created by mc.kim on 7/28/2016.
 */
public class AccessToken  {
    private String access_token = null;
    private String token_secret = null;
    private String familyName = null;

    public String getAccess_token() {
        return access_token;
    }

    public String getToken_secret() {
        return token_secret;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setAccess_token(String token) {
        this.access_token = token;
    }

    public void setToken_secret(String token_secret) {
        this.token_secret = token_secret;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }
}
