package com.himmiractivity.entity;

import java.io.Serializable;

public class Space implements Serializable {
    private UserRoom userRoom;

    private DeviceFreshAirData deviceFreshAirData;

    private BuyAddress buyAddress;

    private Device device;

    private InstallAddress installAddress;

    public class UserRoom {
        private String room_name;

        private int user_room_id;

        private String create_time;

        private String user_key;

        public void setRoom_name(String room_name) {
            this.room_name = room_name;
        }

        public String getRoom_name() {
            return this.room_name;
        }

        public void setUser_room_id(int user_room_id) {
            this.user_room_id = user_room_id;
        }

        public int getUser_room_id() {
            return this.user_room_id;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public void setUser_key(String user_key) {
            this.user_key = user_key;
        }

        public String getUser_key() {
            return this.user_key;
        }
    }

    public void setUserRoom(UserRoom userRoom) {
        this.userRoom = userRoom;
    }

    public UserRoom getUserRoom() {
        return this.userRoom;
    }

    public void setDeviceFreshAirData(DeviceFreshAirData deviceFreshAirData) {
        this.deviceFreshAirData = deviceFreshAirData;
    }

    public DeviceFreshAirData getDeviceFreshAirData() {
        return this.deviceFreshAirData;
    }

    public void setBuyAddress(BuyAddress buyAddress) {
        this.buyAddress = buyAddress;
    }

    public BuyAddress getBuyAddress() {
        return this.buyAddress;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Device getDevice() {
        return this.device;
    }

    public void setInstallAddress(InstallAddress installAddress) {
        this.installAddress = installAddress;
    }

    public InstallAddress getInstallAddress() {
        return this.installAddress;
    }

    public class DeviceFreshAirData {
    }

    public class BuyAddress {
        private String device_sn;

        private String detail_address;

        private int device_buy_address_id;

        private String county_name;

        private String province_name;

        private String buy_time;

        private int county_id;

        private String city_name;

        private int province_id;

        private String town_id;

        private String town_name;

        private String buy_info;

        private int city_id;

        public void setDevice_sn(String device_sn) {
            this.device_sn = device_sn;
        }

        public String getDevice_sn() {
            return this.device_sn;
        }

        public void setDetail_address(String detail_address) {
            this.detail_address = detail_address;
        }

        public String getDetail_address() {
            return this.detail_address;
        }

        public void setDevice_buy_address_id(int device_buy_address_id) {
            this.device_buy_address_id = device_buy_address_id;
        }

        public int getDevice_buy_address_id() {
            return this.device_buy_address_id;
        }

        public void setCounty_name(String county_name) {
            this.county_name = county_name;
        }

        public String getCounty_name() {
            return this.county_name;
        }

        public void setProvince_name(String province_name) {
            this.province_name = province_name;
        }

        public String getProvince_name() {
            return this.province_name;
        }

        public String getBuy_time() {
            return buy_time;
        }

        public void setBuy_time(String buy_time) {
            this.buy_time = buy_time;
        }

        public void setCounty_id(int county_id) {
            this.county_id = county_id;
        }

        public int getCounty_id() {
            return this.county_id;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public String getCity_name() {
            return this.city_name;
        }

        public void setProvince_id(int province_id) {
            this.province_id = province_id;
        }

        public int getProvince_id() {
            return this.province_id;
        }

        public void setTown_id(String town_id) {
            this.town_id = town_id;
        }

        public String getTown_id() {
            return this.town_id;
        }

        public void setTown_name(String town_name) {
            this.town_name = town_name;
        }

        public String getTown_name() {
            return this.town_name;
        }

        public void setBuy_info(String buy_info) {
            this.buy_info = buy_info;
        }

        public String getBuy_info() {
            return this.buy_info;
        }

        public void setCity_id(int city_id) {
            this.city_id = city_id;
        }

        public int getCity_id() {
            return this.city_id;
        }
    }

    public class Device {
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
    }

    public class InstallAddress implements Serializable {
        private String install_time;

        private String device_sn;

        private String detail_address;

        private String county_name;

        private String province_name;

        private int device_install_address_id;

        private int county_id;

        private String city_name;

        private int province_id;

        private String town_id;

        private String town_name;

        private String install_info;

        private int city_id;


        public void setDevice_sn(String device_sn) {
            this.device_sn = device_sn;
        }

        public String getDevice_sn() {
            return this.device_sn;
        }

        public void setDetail_address(String detail_address) {
            this.detail_address = detail_address;
        }

        public String getDetail_address() {
            return this.detail_address;
        }

        public void setCounty_name(String county_name) {
            this.county_name = county_name;
        }

        public String getCounty_name() {
            return this.county_name;
        }

        public String getInstall_time() {
            return install_time;
        }

        public void setInstall_time(String install_time) {
            this.install_time = install_time;
        }

        public void setProvince_name(String province_name) {
            this.province_name = province_name;
        }

        public String getProvince_name() {
            return this.province_name;
        }

        public void setDevice_install_address_id(int device_install_address_id) {
            this.device_install_address_id = device_install_address_id;
        }

        public int getDevice_install_address_id() {
            return this.device_install_address_id;
        }

        public void setCounty_id(int county_id) {
            this.county_id = county_id;
        }

        public int getCounty_id() {
            return this.county_id;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public String getCity_name() {
            return this.city_name;
        }

        public void setProvince_id(int province_id) {
            this.province_id = province_id;
        }

        public int getProvince_id() {
            return this.province_id;
        }

        public void setTown_id(String town_id) {
            this.town_id = town_id;
        }

        public String getTown_id() {
            return this.town_id;
        }

        public void setTown_name(String town_name) {
            this.town_name = town_name;
        }

        public String getTown_name() {
            return this.town_name;
        }

        public void setInstall_info(String install_info) {
            this.install_info = install_info;
        }

        public String getInstall_info() {
            return this.install_info;
        }

        public void setCity_id(int city_id) {
            this.city_id = city_id;
        }

        public int getCity_id() {
            return this.city_id;
        }
    }
}
