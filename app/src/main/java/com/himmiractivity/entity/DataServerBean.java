package com.himmiractivity.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/21.
 */

public class DataServerBean implements Serializable {
    private DataServerConfig dataServerConfig;

    public void setDataServerConfig(DataServerConfig dataServerConfig) {
        this.dataServerConfig = dataServerConfig;
    }

    public DataServerConfig getDataServerConfig() {
        return this.dataServerConfig;
    }

    public class DataServerConfig implements Serializable {
        private String secondary_server_address;

        private int module_report_interval;

        private int data_server_config_id;

        private int heart_package_interval;

        private int secondary_server_port;

        private int proofread_time_interval;

        private int upload_threshold_time;

        private int server_connection_timeout;

        private int primary_server_port;

        private int read_device_interval;

        private int secondary_server_connection;

        private int primary_server_connection;

        private String primary_server_address;

        private int ap_module_switch;

        public void setSecondary_server_address(String secondary_server_address) {
            this.secondary_server_address = secondary_server_address;
        }

        public String getSecondary_server_address() {
            return this.secondary_server_address;
        }

        public void setModule_report_interval(int module_report_interval) {
            this.module_report_interval = module_report_interval;
        }

        public int getModule_report_interval() {
            return this.module_report_interval;
        }

        public void setData_server_config_id(int data_server_config_id) {
            this.data_server_config_id = data_server_config_id;
        }

        public int getData_server_config_id() {
            return this.data_server_config_id;
        }

        public void setHeart_package_interval(int heart_package_interval) {
            this.heart_package_interval = heart_package_interval;
        }

        public int getHeart_package_interval() {
            return this.heart_package_interval;
        }

        public void setSecondary_server_port(int secondary_server_port) {
            this.secondary_server_port = secondary_server_port;
        }

        public int getSecondary_server_port() {
            return this.secondary_server_port;
        }

        public void setProofread_time_interval(int proofread_time_interval) {
            this.proofread_time_interval = proofread_time_interval;
        }

        public int getProofread_time_interval() {
            return this.proofread_time_interval;
        }

        public void setUpload_threshold_time(int upload_threshold_time) {
            this.upload_threshold_time = upload_threshold_time;
        }

        public int getUpload_threshold_time() {
            return this.upload_threshold_time;
        }

        public void setServer_connection_timeout(int server_connection_timeout) {
            this.server_connection_timeout = server_connection_timeout;
        }

        public int getServer_connection_timeout() {
            return this.server_connection_timeout;
        }

        public void setPrimary_server_port(int primary_server_port) {
            this.primary_server_port = primary_server_port;
        }

        public int getPrimary_server_port() {
            return this.primary_server_port;
        }

        public void setRead_device_interval(int read_device_interval) {
            this.read_device_interval = read_device_interval;
        }

        public int getRead_device_interval() {
            return this.read_device_interval;
        }

        public void setSecondary_server_connection(int secondary_server_connection) {
            this.secondary_server_connection = secondary_server_connection;
        }

        public int getSecondary_server_connection() {
            return this.secondary_server_connection;
        }

        public void setPrimary_server_connection(int primary_server_connection) {
            this.primary_server_connection = primary_server_connection;
        }

        public int getPrimary_server_connection() {
            return this.primary_server_connection;
        }

        public void setPrimary_server_address(String primary_server_address) {
            this.primary_server_address = primary_server_address;
        }

        public String getPrimary_server_address() {
            return this.primary_server_address;
        }

        public void setAp_module_switch(int ap_module_switch) {
            this.ap_module_switch = ap_module_switch;
        }

        public int getAp_module_switch() {
            return this.ap_module_switch;
        }
    }
}
