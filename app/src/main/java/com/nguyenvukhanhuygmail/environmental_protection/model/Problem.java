package com.nguyenvukhanhuygmail.environmental_protection.model;

import java.util.List;

/**
 * Created by Uy Nguyen on 9/19/2018.
 */

public class Problem {

    private String title;
    private String describe;
    private List<byte[]> images;
    private int image_num;
    private String date;
    private boolean state;
    private String location;
    private String uID;

    public Problem() {
    }

    public Problem(String title, String describe, List<byte[]> images, int image_num, String date, boolean state, String location, String uID) {
        this.title = title;
        this.describe = describe;
        this.images = images;
        this.image_num = image_num;
        this.date = date;
        this.state = state;
        this.location = location;
        this.uID = uID;
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

    public List<byte[]> getImages() {
        return images;
    }

    public Problem setImages(List<byte[]> images) {
        this.images = images;
        return this;
    }

    public int getImage_num() {
        return image_num;
    }

    public Problem setImage_num(int image_num) {
        this.image_num = image_num;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Problem setDate(String date) {
        this.date = date;
        return this;
    }

    public boolean isState() {
        return state;
    }

    public Problem setState(boolean state) {
        this.state = state;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public Problem setLocation(String location) {
        this.location = location;
        return this;
    }

    public String getID() {
        return uID;
    }

    public Problem setID(String uID) {
        this.uID = uID;
        return this;
    }

}
