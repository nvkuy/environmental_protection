package com.nguyenvukhanhuygmail.environmental_protection.model;

/**
 * Created by Uy Nguyen on 9/19/2018.
 */

public class Problem {

    private String title;
    private String describe;
    private String image_code;
    private String date;
    private String location;
    private int image_num;
    private String uID;

    public Problem() {
    }

    public Problem(String title, String describe, String date, String location, int image_num, String uID) {
        this.uID = uID;
        this.title = title;
        this.describe = describe;
        this.image_code = uID + date;
        this.date = date;
        this.location = location;
        this.image_num = image_num;
    }

    public String getuID() {
        return uID;
    }

    public Problem setuID(String uID) {
        this.uID = uID;
        return this;
    }

    public String getImage_code() {
        return image_code;
    }

    public String getTitle() {
        return title;
    }

    public Problem setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescribe() {
        return describe;
    }

    public Problem setDescribe(String describe) {
        this.describe = describe;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Problem setDate(String date) {
        this.date = date;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public Problem setLocation(String location) {
        this.location = location;
        return this;
    }

    public int getImage_num() {
        return image_num;
    }

    public Problem setImage_num(int image_num) {
        this.image_num = image_num;
        return this;
    }
}
