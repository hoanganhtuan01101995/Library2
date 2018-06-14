package com.alticast.viettelottcommons.resource.response;

public class STBInfo {
    private UserSTB user = null;

    public void setUser(UserSTB user) {
        this.user = user;
    }

    public UserSTB getUser() {
        return user;
    }

    public class UserSTB {
        private String id = null;
        private STBAccount account = null;
        private STBdevice device = null;

        public STBdevice getDevice() {
            return device;
        }

        public String getId() {
            return id;
        }

        public STBAccount getAccount() {
            return account;
        }

    }

    public class STBAccount {
        private String regional = null;
        private String group = null;

        public String getRegional() {
            return regional;
        }

        public String getGroup() {
            return group;
        }

    }

    public class STBdevice {
        private String udid = null;
        private String type = null;
        private String category = null;
        private String ip = null;

        public String getUdid() {
            return udid;
        }

        public String getType() {
            return type;
        }

        public String getCategory() {
            return category;
        }

        public String getIp() {
            return ip;
        }

    }

}
