package com.alticast.viettelottcommons.resource.request;

import com.alticast.viettelottcommons.loader.PlaybackLoader;

/**
 * Created by duyuno on 10/12/16.
 */
public class LivePrepareBody {
    public int version;
    public String regionId;
    public String assetId;
    public String channelId;
    public String client_id;
    public String filename;
    public String manifestType;
    public String userId;
    public String bwProfile;
    public String hash;
    public String serviceProvider = PlaybackLoader.PROVIDER;

    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public void setBwProfile(String bwProfile) {
        this.bwProfile = bwProfile;
    }

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

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
