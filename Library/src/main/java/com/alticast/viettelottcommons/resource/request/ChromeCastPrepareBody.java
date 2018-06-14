package com.alticast.viettelottcommons.resource.request;

/**
 * Created by duyuno on 10/12/16.
 */
public class ChromeCastPrepareBody {
    public int version;
    public String regionId;
    public String assetId;
    public String filename;
    public String manifestType;
    public String userId;

    public String getBwProfile() {
        return bwProfile;
    }

    public void setBwProfile(String bwProfile) {
        this.bwProfile = bwProfile;
    }

    public String getUserDeviceType() {
        return userDeviceType;
    }

    public void setUserDeviceType(String userDeviceType) {
        this.userDeviceType = userDeviceType;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String bwProfile;
    public String userDeviceType;
    public String categoryId;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getManifestType() {
        return manifestType;
    }

    public void setManifestType(String manifestType) {
        this.manifestType = manifestType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
