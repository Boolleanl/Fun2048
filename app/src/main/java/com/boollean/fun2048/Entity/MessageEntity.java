package com.boollean.fun2048.Entity;

import com.google.gson.annotations.SerializedName;

/**
 * 留言板对象基类
 *
 * @author Boollean
 */
public class MessageEntity {
    @SerializedName("name")
    private String name;
    @SerializedName("gender")
    private int gender;
    @SerializedName("date")
    private String date;
    @SerializedName("message")
    private String message;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
