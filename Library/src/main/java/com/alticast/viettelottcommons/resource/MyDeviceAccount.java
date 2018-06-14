package com.alticast.viettelottcommons.resource;

import android.os.Parcel;
import android.os.Parcelable;

import com.alticast.viettelottcommons.util.Logger;

/**
 * Created by mc.kim on 7/28/2016.
 */
public class MyDeviceAccount extends ApiError {

    private String id = null;
    private String name = null;
    private String display_name = null;
    private String status = null;
    private String region = null;
    private String so_id = null;
    private String address = null;
    private String email = null;
    private String cellphone = null;
    private String gender = null;
    private String subscription_date = null;
    private Config config = null;
    private Devices[] devices = null;
    private Pairing pairing = null;

    private RegistedDevice registered_device;

    public class Config {
        private String selected_payment_option = null;
        private boolean vm_subscriber = false;

        public String getSelected_payment_option() {
            return selected_payment_option;
        }

        public void setSelected_payment_option(String selected_payment_option) {
            this.selected_payment_option = selected_payment_option;
        }

        public boolean isVm_subscriber() {
            return vm_subscriber;
        }

        public boolean isViettelMobileBalanceUser() {
            Logger.print("HandheldAuthorization ", "isViettelMobileBalanceUser vm_subscriber  " + vm_subscriber + " selected_payment_option " + selected_payment_option
                    + " selected_payment_option.toUpperCase() " + selected_payment_option.toUpperCase());
            return vm_subscriber || (selected_payment_option != null && selected_payment_option.toUpperCase().equals("MB"));
        }
    }

    public String getPaymentOption() {
        return config != null ? config.getSelected_payment_option() : null;
    }

    public void setPaymentOption(String method) {
        if(config != null) {
            config.setSelected_payment_option(method);
        }
    }


    public static class Devices implements Parcelable {
        private String id = null;
        private String model = null;
        private String model_no = null;
        private String model_name = null;
        private String os_name = null;
        private String os_version = null;
        private String manufacturer = null;
        private String type = null;
        private String last_date = null;

        protected Devices(Parcel in) {
            id = in.readString();
            model = in.readString();
            model_no = in.readString();
            model_name = in.readString();
            os_name = in.readString();
            os_version = in.readString();
            manufacturer = in.readString();
            type = in.readString();
            last_date = in.readString();
        }

        public Devices(String model_name, String type) {
            this.model_name = model_name;
            this.type = type;
        }

        public static final Creator<Devices> CREATOR = new Creator<Devices>() {
            @Override
            public Devices createFromParcel(Parcel in) {
                return new Devices(in);
            }

            @Override
            public Devices[] newArray(int size) {
                return new Devices[size];
            }
        };

        public String getId() {
            return id;
        }

        public String getModel() {
            return model;
        }

        public String getModel_no() {
            return model_no;
        }

        public String getModel_name() {
            return model_name;
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

        public String getLast_date() {
            return last_date;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(model);
            dest.writeString(model_no);
            dest.writeString(model_name);
            dest.writeString(os_name);
            dest.writeString(os_version);
            dest.writeString(manufacturer);
            dest.writeString(type);
            dest.writeString(last_date);
        }
    }


    public class Pairing {
        private String family_id = null;
        private String device_id = null;
        private String paired_date = null;
        private String paired_alias = null;
        private String device_udid = null;
        private String handheld_id = null;

        public String getHandheld_id() {
            return handheld_id;
        }

        public String getFamily_id() {
            return family_id;
        }

        public String getDevice_id() {
            return device_id;
        }

        public String getPaired_date() {
            return paired_date;
        }

        public String getDevice_udid() {
            return device_udid;
        }

        public String getPaired_alias() {
            return paired_alias;
        }
    }


    public RegistedDevice getRegistered_device() {
        return registered_device;
    }

    public void setRegistered_device(RegistedDevice registered_device) {
        this.registered_device = registered_device;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public String getRegion() {
        return region;
    }
    public String getStatus() {
        return status;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getCellphone() {
        return cellphone;
    }

    public String getGender() {
        return gender;
    }

    public String getSubscription_date() {
        return subscription_date;
    }

    public Config getConfig() {
        return config;
    }

    public Devices[] getDevices() {
        return devices;
    }

    public Pairing getPairing() {
        return pairing;
    }

    public Devices getDevice(String id) {
        if (devices == null) {
            return null;
        }

        int size = devices.length;
        if (size == 0) {
            return null;
        }

        Devices device = null;
        for (int i = 0; i < size; i++) {
            String deviceId = devices[i].getId();

            if (deviceId.equalsIgnoreCase(id)) {
                device = devices[i];
                break;
            }
        }
        return device;
    }


}
