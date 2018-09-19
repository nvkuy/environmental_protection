package com.nguyenvukhanhuygmail.environmental_protection.model;

/**
 * Created by Uy Nguyen on 9/19/2018.
 */

public class User {

    private String acc_type;
    private String location;
    private String phone_number;
    private String sex;
    private String user_name;

    public User() {
    }

    public User(String acc_type, String location, String phone_number, String sex, String user_name) {
        this.acc_type = acc_type;
        this.location = location;
        this.phone_number = phone_number;
        this.sex = sex;
        this.user_name = user_name;
    }

    public String getAcc_type() {
        return acc_type;
    }

    public User setAcc_type(String acc_type) {
        this.acc_type = acc_type;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public User setLocation(String location) {
        this.location = location;
        return this;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public User setPhone_number(String phone_number) {
        this.phone_number = phone_number;
        return this;
    }

    public String getSex() {
        return sex;
    }

    public User setSex(String sex) {
        this.sex = sex;
        return this;
    }

    public String getUser_name() {
        return user_name;
    }

    public User setUser_name(String user_name) {
        this.user_name = user_name;
        return this;
    }
}
