package com.himmiractivity.entity;

/**
 * Created by Administrator on 2017/4/26.
 */

public class VersionBean {
    private Version appVerInfo;

    public Version getAppVerInfo() {
        return appVerInfo;
    }

    public void setAppVerInfo(Version appVerInfo) {
        this.appVerInfo = appVerInfo;
    }

    public class Version {
        String version_name;
        String address;
        String updata_content;
        int version_code;
        int app_id;

        public String getVersion_name() {
            return version_name;
        }

        public String getAddress() {
            return address;
        }

        public String getUpdata_content() {
            return updata_content;
        }

        public int getVersion_code() {
            return version_code;
        }

        public int getApp_id() {
            return app_id;
        }

        public void setVersion_name(String version_name) {
            this.version_name = version_name;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setUpdata_content(String updata_content) {
            this.updata_content = updata_content;
        }

        public void setVersion_code(int version_code) {
            this.version_code = version_code;
        }

        public void setApp_id(int app_id) {
            this.app_id = app_id;
        }
    }
}
