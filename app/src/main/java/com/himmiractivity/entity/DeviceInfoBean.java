package com.himmiractivity.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/21.
 */

public class DeviceInfoBean implements Serializable {
    DeviceInfo deviceInfo;

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public class DeviceInfo implements Serializable {
        private String device_shipmenttime;

        private String device_mac;

        private int device_id;

        private String device_sn;

        private String device_model;

        private String device_2code;

        private String device_producttime;

        private String device_type;

        private String device_keyintime;


        public void setDevice_mac(String device_mac) {
            this.device_mac = device_mac;
        }

        public String getDevice_mac() {
            return this.device_mac;
        }

        public String getDevice_shipmenttime() {
            return device_shipmenttime;
        }

        public String getDevice_producttime() {
            return device_producttime;
        }

        public String getDevice_keyintime() {
            return device_keyintime;
        }

        public void setDevice_shipmenttime(String device_shipmenttime) {
            this.device_shipmenttime = device_shipmenttime;
        }

        public void setDevice_producttime(String device_producttime) {
            this.device_producttime = device_producttime;
        }

        public void setDevice_keyintime(String device_keyintime) {
            this.device_keyintime = device_keyintime;
        }

        public void setDevice_id(int device_id) {
            this.device_id = device_id;
        }

        public int getDevice_id() {
            return this.device_id;
        }

        public void setDevice_sn(String device_sn) {
            this.device_sn = device_sn;
        }

        public String getDevice_sn() {
            return this.device_sn;
        }

        public void setDevice_model(String device_model) {
            this.device_model = device_model;
        }

        public String getDevice_model() {
            return this.device_model;
        }

        public void setDevice_2code(String device_2code) {
            this.device_2code = device_2code;
        }

        public String getDevice_2code() {
            return this.device_2code;
        }


        public void setDevice_type(String device_type) {
            this.device_type = device_type;
        }

        public String getDevice_type() {
            return this.device_type;
        }

    }
}
